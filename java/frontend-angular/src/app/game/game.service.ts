import {Injectable} from '@angular/core';
import {GameServiceAbstract} from "../services/game-service-abstract";
import {BehaviorSubject, map, Observable, of, ReplaySubject} from "rxjs";
import {
  AnswerCode,
  AnswerDto,
  GameLog,
  GameResponseDto,
  QuestionDto,
  TriviaControllerService
} from "../openapi-generated";
import {IMessage} from "@stomp/rx-stomp";
import {RxStompService} from "../adapters/websockets/rx-stomp.service";
import {User} from "../user/user";
import {gameDtoToGame, gameToGameDto, userToUserDto} from "../common/helpers";
import {Game} from "./game";

@Injectable({
  providedIn: 'root'
})
export class GameService extends GameServiceAbstract {
  private gamesSubject = new BehaviorSubject<Game[]>([])
  private isGameListInitialized: boolean = false
  private gamesSubjectsMap = new Map<number, ReplaySubject<Game>>()
  private isUpdateHandlerRegistered = new Set<number>()
  private gameLogsSubjects = new BehaviorSubject<GameLog[]>([])
  private gameLogs$ = this.gameLogsSubjects.asObservable()
  private isGameLogsHandlerRegistered = new Set<number>()

  constructor(private openApiService: TriviaControllerService,
              private rxStompService: RxStompService) {
    super();
  }

  override rollDice(gameId: number, userId: string): Observable<Game> {
    return this.openApiService.rollDice(gameId, userId)
      .pipe(map(dto => gameDtoToGame(dto)));
  }

  override drawQuestion(gameId: number, userId: string): Observable<QuestionDto> {
    return this.openApiService.drawQuestion(gameId, userId)
      .pipe(map(value => value.currentQuestion!));
  }

  override answerQuestion(gameId: number, userId: string, answerCode: AnswerCode): Observable<AnswerDto> {
    return this.openApiService.answer(gameId, userId, answerCode);
  }

  override validate(gameId: number, userId: string): Observable<void> {
    return this.openApiService.validate(gameId, userId);
  }

  override getGames() {
    if (!this.isGameListInitialized) {
      this.initGamesList();
      this.isGameListInitialized = true
    }
    return this.gamesSubject.asObservable();
  }

  initGamesList() {
    this.openApiService.listGames()
      .pipe(
        map(dtos => dtos.map(dto => gameDtoToGame(dto)))
      )
      .subscribe(games => {
        this.gamesSubject.next(games);
        for (let game of games) {
          this.registerGameUpdatedHandler(game.id);
        }
      })
    // TODO get Games => enregistrement automatique des handlers, à revoir
    this.registerGameCreatedHandler();
    this.registerGameDeletedHandler();
  }

  private addSingleGameSubject(game: Game, gameId: number = game.id) {
    this.createSubjectForSingleGame(gameId);
    this.gamesSubjectsMap.get(gameId)!.next(game);
  }

  initSingleGame(gameId: number) {
    this.createSubjectForSingleGame(gameId);
    let gameObservable = this.openApiService.getGameById(gameId)
      .pipe(map( dto => gameDtoToGame(dto)));
    gameObservable
      .subscribe({
        next: game => {
          this.addSingleGameSubject(game)
          this.registerGameUpdatedHandler(game.id);
        },
        error: err => {
          this.gamesSubjectsMap.get(gameId)?.error(err);
          this.registerGameUpdatedHandler(gameId);
        }
      });
  }

  private createSubjectForSingleGame(gameId: number) {
    if (!this.gamesSubjectsMap.has(gameId)) {
      this.gamesSubjectsMap.set(gameId, new ReplaySubject<Game>(1));
    }
  }

  initGameLogs(gameId: number) {
    this.openApiService.getGameLogs(gameId)
      .subscribe(gameLogs => {
        this.gameLogsSubjects.next(this.splitLogs(gameLogs));
      })
    this.registerNewGameLogHandler(gameId);
  }

  splitLogs(gameLogs: GameLog[]): GameLog[] {
    return gameLogs.flatMap(
      (g) => g.value.split('\n')
        .filter(text => text.trim() != "")
        .map(text => buildGameLog(text, g)))
      ;

    function buildGameLog(text: string, g: GameLog) {
      return {
        value: text,
        gameId: g.gameId
      } as GameLog;
    }
  }

  private registerNewGameLogHandler(gameId: number) {
    if (!this.isGameLogsHandlerRegistered.has(gameId)) {
      this.isGameLogsHandlerRegistered.add(gameId)
      this.rxStompService.watch(`/topic/games/${gameId}/logs`)
        .subscribe((message: IMessage) => {
          let newGameLog = JSON.parse(message.body) as GameLog;
          this.handleNewGameLog(newGameLog);
        });
    }
  }

  public handleNewGameLog(newGameLog: GameLog) {
    let logsToAdd = newGameLog.value
      .split('\n')
      .filter(value => value.trim())
      .map((m) => {
        return {gameId: 1, value: m} as GameLog;
      });
    this.gameLogsSubjects.next(this.gameLogsSubjects.value.concat(logsToAdd))
  }

  override create(name: string, user: User): Observable<Game> {
    let requestDto = {gameName: name, creator: userToUserDto(user)};
    return this.openApiService.createGame(requestDto)
      .pipe(
        map(dto => gameDtoToGame(dto)),
      );
  }

  override getGame(gameId: number): Observable<Game> {
    if (!this.gamesSubjectsMap.has(gameId)) {
      this.initSingleGame(gameId)
    }
    return this.gamesSubjectsMap.get(gameId)!.asObservable();
  }

  override delete(gameId: number): Observable<any> {
    return this.openApiService.deleteGameById(gameId)
  }

  override start(gameId: number, userId: string): Observable<Game> {
    return this.openApiService.startGame(gameId, userId)
      .pipe(map(dto => gameDtoToGame(dto)));
  }

  override join(game: Game, user: User): Observable<Game> {
    return this.openApiService.addPlayerToGame(game.id, user.id, userToUserDto(user))
      .pipe(map(dto => gameDtoToGame(dto)));
  }

  override getGameLogs(gameId: number): Observable<Array<GameLog>> {
    return this.gameLogs$;
  }

  registerGameCreatedHandler() {
    this.rxStompService.watch(`/topic/games/created`).subscribe((message: IMessage) => {
      let newGameDto = JSON.parse(message.body) as GameResponseDto;
      let newGame = gameDtoToGame(newGameDto);
      this.handleGameCreated(newGame);
    });
  }

  private handleGameCreated(newGame: Game) {
    this.addToGameListSubject(newGame);
    this.addSingleGameSubject(newGame, newGame.id)
    this.registerGameUpdatedHandler(newGame.id)
  }

  registerGameUpdatedHandler(gameId: number) {
    if (!this.isUpdateHandlerRegistered.has(gameId)) {
      this.isUpdateHandlerRegistered.add(gameId)
      this.rxStompService.watch(`/topic/games/${gameId}`).subscribe((message: IMessage) => {
        let updatedGameDto = JSON.parse(message.body) as GameResponseDto;
        let updatedGame = gameDtoToGame(updatedGameDto);
        this.handleGameUpdated(updatedGame);
      });
    }
  }

  private handleGameUpdated(updatedGame: Game) {
    this.addSingleGameSubject(updatedGame);
    this.updateGameFromSubjectList(updatedGame)
  }

  registerGameDeletedHandler() {
    this.rxStompService.watch(`/topic/games/deleted`).subscribe((message: IMessage) => {
      let deletedGameId: number = Number.parseInt(message.body);
      this.handleGameDeleted(deletedGameId);
    });
  }

  private handleGameDeleted(deletedGameId: number) {
    this.deleteGameFromSubjects(deletedGameId);
    this.gamesSubjectsMap.delete(deletedGameId);
  }

  private addToGameListSubject(game: Game) {
    this.gamesSubject.next([...this.gamesSubject.value, game])
  }

  private updateGameFromSubjectList(game: Game) {
    let replacement = this.gamesSubject.value;
    const index = replacement.findIndex(
      g => g.id === game.id);
    if (index !== -1) {
      replacement[index] = game
    }
    this.gamesSubject.next(replacement)
  }

  private deleteGameFromSubjects(gameId: number) {
    this.gamesSubject.next([...this.gamesSubject.value.filter(game => game.id !== gameId)])
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
