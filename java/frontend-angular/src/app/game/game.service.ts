import {Injectable} from '@angular/core';
import {GameServiceAbstract} from "../services/game-service-abstract";
import {BehaviorSubject, map, Observable, of, Subject} from "rxjs";
import {GameLog, TriviaControllerService} from "../openapi-generated";
import {IMessage} from "@stomp/rx-stomp";
import {RxStompService} from "../adapters/websockets/rx-stomp.service";
import {User} from "../user/user";
import {userToUserDto} from "../common/helpers";
import {Game} from "./game";

@Injectable({
  providedIn: 'root'
})
export class GameService extends GameServiceAbstract {
  private gamesSubject = new BehaviorSubject<Game[]>([])
  private games$ = this.gamesSubject.asObservable()
  private gameSubject = new Subject<Game>()
  private game$ = this.gameSubject.asObservable()
  private gameLogsSubjects = new BehaviorSubject<GameLog[]>([])
  private gameLogs$ = this.gameLogsSubjects.asObservable()

  constructor(private openApiService: TriviaControllerService,
              private rxStompService: RxStompService) {
    super();
  }

  override getGames() {
    return this.games$;
  }

  initGamesListHandlers() {
    this.openApiService.listGames()
      .pipe(
        map(dtos => dtos.map(Game.fromDto))
      )
      .subscribe(games => {
        this.gamesSubject.next(games);
        for (let game of games) {
          this.registerGameUpdatedHandler(game);
        }
      })
    this.registerGameCreatedHandler();
    this.registerGameDeletedHandler();
  }

  initGameLogs(gameId: number) {
    this.openApiService.getGameLogs(gameId)
      .subscribe(gameLogs => this.gameLogsSubjects.next(gameLogs))
    this.registerNewGameLogHandler(gameId);
  }

  private registerNewGameLogHandler(gameId: number) {
    this.rxStompService.watch(`/topic/games/${gameId}/logs`)
      .subscribe((message: IMessage) => {
        let newGameLog = JSON.parse(message.body) as GameLog;
        this.gameLogsSubjects.next([...this.gameLogsSubjects.value, newGameLog])
      });
  }

  override create(name: string, user: User): Observable<Game> {
    let requestDto = {gameName: name, creator: userToUserDto(user)};
    return this.openApiService.createGame(requestDto)
      .pipe(
        map(Game.fromDto)
      );
  }

  override getGame(gameId: number): Observable<Game> {
    this.openApiService.getGameById(gameId)
      .pipe(map(Game.fromDto))
      .subscribe(game => {
          this.gameSubject.next(game);
          this.registerGameUpdatedHandler(game);
        }
      );
    return this.game$;
  }

  override delete(gameId: number): Observable<any> {
    return this.openApiService.deleteGameById(gameId)
  }

  override playTurn(gameId: number, userId: string): Observable<Game> {
    return this.openApiService.playTurn(gameId, userId)
      .pipe(map(Game.fromDto));
  }

  override start(gameId: number, userId: string): Observable<Game> {
    return this.openApiService.startGame(gameId, userId)
      .pipe(map(Game.fromDto));
  }

  override join(game: Game, user: User): Observable<Game> {
    return this.openApiService.addPlayerToGame(game.id, user.id, userToUserDto(user))
      .pipe(map(Game.fromDto));
  }

  override getGameLogs(gameId: number): Observable<Array<GameLog>> {
    return this.openApiService.getGameLogs(gameId)
  }

  getGameLogs2(gameId: number): Observable<Array<GameLog>> {
    return this.gameLogs$;
  }

  private registerGameCreatedHandler() {
    this.rxStompService.watch(`/topic/games/created`).subscribe((message: IMessage) => {
      let newGame = JSON.parse(message.body);
      this.addGameToSubjectList(newGame)
      this.registerGameUpdatedHandler(newGame)
    });
  }

  private registerGameUpdatedHandler(game: Game) {
    this.rxStompService.watch(`/topic/games/${game.id}`).subscribe((message: IMessage) => {
      let updatedGame = JSON.parse(message.body);
      this.gameSubject.next(updatedGame);
      this.updateGameFromSubjectList(updatedGame)
    });
  }

  private registerGameDeletedHandler() {
    this.rxStompService.watch(`/topic/games/deleted`).subscribe((message: IMessage) => {
      let deletedGameId: number = Number.parseInt(message.body);
      this.deleteGameFromSubjects(deletedGameId);
    });
  }

  private addGameToSubjectList(game: Game) {
    this.gamesSubject.next([...this.gamesSubject.value, game])
  }

  private updateGameFromSubjectList(game: Game) {
    let replacement = this.gamesSubject.value;
    const index = replacement.findIndex(
      g => g.id === game.id);
    if (index !== -1) {
      replacement.splice(index, 1, game);
    }
    this.gamesSubject.next(replacement)
  }

  private deleteGameFromSubjects(gameId: number) {
    console.log(`coucou deleteGame$`)
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
