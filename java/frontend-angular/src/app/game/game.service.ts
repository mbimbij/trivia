import {Injectable} from '@angular/core';
import {GameServiceAbstract} from "../services/game-service-abstract";
import {BehaviorSubject, catchError, map, Observable, of, Subject} from "rxjs";
import {GameLog, GameResponseDto, TriviaControllerService} from "../openapi-generated";
import {IMessage} from "@stomp/rx-stomp";
import {RxStompService} from "../adapters/websockets/rx-stomp.service";
import {User} from "../user/user";
import {playerDtoToPlayer, userToUserDto} from "../common/helpers";
import {Game} from "./game";

@Injectable({
  providedIn: 'root'
})
export class GameService extends GameServiceAbstract {
  private gamesSubject = new BehaviorSubject<Game[]>([])
  games$ = this.gamesSubject.asObservable()

  override getGames() {
    console.log(`coucou getGames$`)
    return this.games$;
  }

  addGame$(game: Game) {
    console.log(`coucou addGame$`)
    this.gamesSubject.next([...this.gamesSubject.value, game])
  }

  deleteGame$(gameId: number) {
    console.log(`coucou deleteGame$`)
    this.gamesSubject.next([...this.gamesSubject.value.filter(game => game.id !== gameId)])
  }

  updateGame$(game: Game) {
    console.log(`coucou updateGame$`)
    let replacement = this.gamesSubject.value;
    const index = replacement.findIndex(
      g => g.id === game.id);
    if (index !== -1) {
      replacement.splice(index, 1, game);
    }
    this.gamesSubject.next(replacement)
  }

  constructor(private service: TriviaControllerService,
              private rxStompService: RxStompService) {
    super();
  }

  doInit() {
    this.service.listGames()
      .pipe(
        map(dtos =>
          dtos.map(dto => new Game(dto.id,
            dto.name,
            dto.state,
            dto.turn,
            playerDtoToPlayer(dto.creator),
            playerDtoToPlayer(dto.currentPlayer),
            dto.players.map(
              playerDto => playerDtoToPlayer(playerDto)
            )
          )))
      )
      .subscribe(games => {
        this.gamesSubject.next(games);
        for (const game of games) {
          this.rxStompService.watch(`/topic/games/${game.id}`).subscribe((message: IMessage) => {
            let updatedGame = JSON.parse(message.body);
            // this.gameUpdatedSubjects.get(game.id)!.next(updatedGame);
            this.updateGame$(updatedGame)
          });
        }
      })

    this.rxStompService.watch(`/topic/games/created`).subscribe((message: IMessage) => {
      let newGame = JSON.parse(message.body);
      this.gameCreatedSubject.next(newGame);
      this.addGame$(newGame)
    });

    this.rxStompService.watch(`/topic/games/deleted`).subscribe((message: IMessage) => {
      let deletedGameId: number = Number.parseInt(message.body);
      this.gameDeletedSubject.next(deletedGameId);
      this.deleteGame$(deletedGameId);
    });
  }

  override create(name: string, user: User): Observable<Game> {
    return this.service.createGame({gameName: name, creator: userToUserDto(user)})
      .pipe(map((game: GameResponseDto) => Game.fromDto(game)));
  }

  override delete(gameId: number): Observable<any> {
    return this.service.deleteGameById(gameId)
  }

  private gameCreatedSubject = new Subject<GameResponseDto>()
  private gameDeletedSubject = new Subject<number>()
  private gameUpdatedSubjects = new Map<number, Subject<GameResponseDto>>()
  private gameLogsAddedSubjects = new Map<number, Subject<GameLog>>()

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

  getGame(gameId: number): Observable<Game> {
    // TODO Réfléchir à comment designer ce machin pour qu'il passe à l'échelle, cad potentiellement des millions de parties
    if (!this.gameUpdatedSubjects.has(gameId)) {
      this.gameUpdatedSubjects.set(gameId, new Subject<GameResponseDto>());
    }
    this.service.getGameById(gameId).subscribe(game => {
        this.gameUpdatedSubjects.get(gameId)!.next(game);
      }
    );
    this.rxStompService.watch(`/topic/games/${gameId}`).subscribe((message: IMessage) => {
      let updatedGame = JSON.parse(message.body);
      this.gameUpdatedSubjects.get(gameId)!.next(updatedGame);
    });
    return this.gameUpdatedSubjects.get(gameId)!.asObservable()
      .pipe(map((game: GameResponseDto) => Game.fromDto(game)));
  }

  override playTurn(gameId: number, userId: string): Observable<Game> {
    return this.service.playTurn(gameId, userId)
      .pipe(
        map(dto => Game.fromDto(dto))
      );
  }

  override start(gameId: number, userId: string): Observable<Game> {
    return this.service.startGame(gameId, userId)
      .pipe(map((game: GameResponseDto) => Game.fromDto(game)));
  }

  override join(game: Game, user: User): Observable<Game> {
    return this.service.addPlayerToGame(game.id, user.id, userToUserDto(user))
      .pipe(map(dto => Game.fromDto(dto)));
  }

  override getGameLogs(gameId: number): Observable<Array<GameLog>> {
    return this.service.getGameLogs(gameId)
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
