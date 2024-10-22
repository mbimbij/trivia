import {Injectable} from '@angular/core';
import {GameServiceAbstract} from "../services/game-service-abstract";
import {BehaviorSubject, map, Observable, of, ReplaySubject} from "rxjs";
import {IMessage} from "@stomp/rx-stomp";
import {RxStompService} from "../adapters/websockets/rx-stomp.service";
import {User} from "../user/user";
import {gameDtoToGame, userToUserDto} from "../shared/helpers";
import {Game} from "./game";
import {
  AnswerCode,
  AnswerDto,
  CreateGameRequestDto,
  GameControllerService,
  GameResponseDto,
  QuestionDto,
  UserDto
} from "../openapi-generated/game";

@Injectable({
  providedIn: 'root'
})
export class GameService extends GameServiceAbstract {
  private gamesSubject = new BehaviorSubject<Game[]>([])
  private isGameListInitialized: boolean = false
  private gamesSubjectsMap = new Map<number, ReplaySubject<Game>>()
  private isUpdateHandlerRegistered = new Set<number>()

  constructor(private gameOpenApiService: GameControllerService,
              private rxStompService: RxStompService) {
    super();
  }

  override rollDice(gameId: number, userId: string): Observable<Game> {
    return this.gameOpenApiService.rollDice(gameId, userId)
      .pipe(map(dto => gameDtoToGame(dto)));
  }

  override drawQuestion(gameId: number, userId: string): Observable<QuestionDto> {
    return this.gameOpenApiService.drawQuestion(gameId, userId)
      .pipe(map(value => value.currentQuestion!));
  }

  override answerQuestion(gameId: number, userId: string, answerCode: AnswerCode): Observable<AnswerDto> {
    return this.gameOpenApiService.answer(gameId, userId, answerCode);
  }

  override validate(gameId: number, userId: string): Observable<void> {
    return this.gameOpenApiService.validate(gameId, userId);
  }

  override getGames() {
    if (!this.isGameListInitialized) {
      this.initGamesList();
      this.isGameListInitialized = true
    }
    return this.gamesSubject.asObservable();
  }

  initGamesList() {
    this.gameOpenApiService.listGames()
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
    let gameObservable = this.gameOpenApiService.getGameById(gameId)
      .pipe(map(dto => gameDtoToGame(dto)));
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

  override create(gameName: string, creator: UserDto): Observable<Game> {
    return this.gameOpenApiService.createGame({gameName: gameName, creator: creator} as CreateGameRequestDto)
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

  override delete(gameId: number): Observable<void> {
    return this.gameOpenApiService.deleteGameById(gameId)
  }

  override start(gameId: number, userId: string): Observable<Game> {
    return this.gameOpenApiService.startGame(gameId, userId)
      .pipe(map(dto => gameDtoToGame(dto)));
  }

  override join(gameId: number, userDto: UserDto): Observable<Game> {
    return this.gameOpenApiService.addPlayerToGame(gameId, userDto.id, userDto)
      .pipe(map(dto => gameDtoToGame(dto)));
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
