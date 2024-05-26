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

  constructor(private service: TriviaControllerService,
              private rxStompService: RxStompService) {
    super();
  }

  override getGames() {
    return this.games$;
  }

  doInit() {
    this.service.listGames()
      .pipe(
        map(dtos => dtos.map(Game.fromDto))
      )
      .subscribe(games => {
        this.gamesSubject.next(games);
        for (let game of games) {
          this.registerGameUpdatedObserver(game);
        }
      })

    this.rxStompService.watch(`/topic/games/created`).subscribe((message: IMessage) => {
      let newGame = JSON.parse(message.body);
      this.addGameToSubjects(newGame)
      // TODO améliorer la lisibilité de la logique d'update des parties nouvellement créées
      this.registerUpdateNotificationsForSingleGame(newGame.id)
    });

    this.rxStompService.watch(`/topic/games/deleted`).subscribe((message: IMessage) => {
      let deletedGameId: number = Number.parseInt(message.body);
      this.deleteGameFromSubjects(deletedGameId);
    });
  }

  override create(name: string, user: User): Observable<Game> {
    let requestDto = {gameName: name, creator: userToUserDto(user)};
    return this.service.createGame(requestDto)
      .pipe(
        map(Game.fromDto)
      );
  }

  override getGame(gameId: number): Observable<Game> {
    // TODO Réfléchir à comment designer ce machin pour qu'il passe à l'échelle, cad potentiellement des millions de parties
    this.registerUpdateNotificationsForSingleGame(gameId);
    this.service.getGameById(gameId)
      .pipe(map(Game.fromDto))
      .subscribe(game => {
          this.gameUpdatedSubjects.get(gameId)!.next(game);
        }
      );
    return this.gameUpdatedSubjects.get(gameId)!.asObservable();
  }

  private registerUpdateNotificationsForSingleGame(gameId: number) {
    if (!this.gameUpdatedSubjects.has(gameId)) {
      this.gameUpdatedSubjects.set(gameId, new Subject<Game>());
    }
    this.rxStompService.watch(`/topic/games/${gameId}`).subscribe((message: IMessage) => {
      let updatedGame = JSON.parse(message.body) as Game;
      this.gameUpdatedSubjects.get(gameId)!.next(updatedGame);
      this.updateGameFromList(updatedGame)
    });
  }

  override delete(gameId: number): Observable<any> {
    return this.service.deleteGameById(gameId)
  }

  private gameUpdatedSubjects = new Map<number, Subject<Game>>()

  private gameLogsAddedSubjects = new Map<number, Subject<GameLog>>()

  private addGameToSubjects(game: Game) {
    this.gamesSubject.next([...this.gamesSubject.value, game])
  }

  private updateGameFromList(game: Game) {
    let replacement = this.gamesSubject.value;
    const index = replacement.findIndex(
      g => g.id === game.id);
    if (index !== -1) {
      replacement.splice(index, 1, game);
    }
    this.gamesSubject.next(replacement)
  }

  private registerGameUpdatedObserver(game: Game) {
    this.rxStompService.watch(`/topic/games/${game.id}`).subscribe((message: IMessage) => {
      let updatedGame = JSON.parse(message.body);
      this.updateGameFromList(updatedGame)
      this.registerUpdateNotificationsForSingleGame(game.id)
    });
  }

  override playTurn(gameId: number, userId: string): Observable<Game> {
    return this.service.playTurn(gameId, userId)
      .pipe(map(Game.fromDto));
  }

  override start(gameId: number, userId: string): Observable<Game> {
    return this.service.startGame(gameId, userId)
      .pipe(map(Game.fromDto));
  }

  override join(game: Game, user: User): Observable<Game> {
    return this.service.addPlayerToGame(game.id, user.id, userToUserDto(user))
      .pipe(map(Game.fromDto));
  }

  private deleteGameFromSubjects(gameId: number) {
    console.log(`coucou deleteGame$`)
    this.gamesSubject.next([...this.gamesSubject.value.filter(game => game.id !== gameId)])
  }

  override getGameLogs(gameId: number): Observable<Array<GameLog>> {
    return this.service.getGameLogs(gameId)
  }

  registerGameLogsObserver(gameId: number, observer: (gameLog: GameLog) => void) {
    if (!this.gameLogsAddedSubjects.has(gameId)) {
      this.gameLogsAddedSubjects.set(gameId, new Subject<GameLog>());
      this.rxStompService.watch(`/topic/games/${gameId}/logs`).subscribe((message: IMessage) => {
        let updatedGame = JSON.parse(message.body);
        this.gameLogsAddedSubjects.get(gameId)!.next(updatedGame);
      });
    }
    this.gameLogsAddedSubjects.get(gameId)!.subscribe(observer)
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
