import {Injectable} from '@angular/core';
import {GameServiceAbstract} from "./game-service-abstract";
import {catchError, Observable, of, Subject} from "rxjs";
import {GameLog, GameResponseDto, TriviaControllerService, UserDto} from "./openapi-generated";
import {IMessage} from "@stomp/rx-stomp";
import {RxStompService} from "./rx-stomp.service";

@Injectable({
  providedIn: 'root'
})
export class GameService extends GameServiceAbstract {
  override delete(gameId: number): Observable<any> {
      return this.service.deleteGameById(gameId)
  }
  private gameCreatedSubject = new Subject<GameResponseDto>()
  private gameDeletedSubject = new Subject<number>()
  private gameUpdatedSubjects = new Map<number, Subject<GameResponseDto>>()
  private gameLogsAddedSubjects = new Map<number, Subject<GameLog>>()
  constructor(private service: TriviaControllerService,
              private rxStompService: RxStompService) {
    super();
  }

  override registerGameCreatedObserver(observer: (newGame: GameResponseDto) => void): void {
    this.gameCreatedSubject.subscribe(observer);
    this.rxStompService.watch(`/topic/games/created`).subscribe((message: IMessage) => {
      let newGame = JSON.parse(message.body);
      this.gameCreatedSubject.next(newGame);
    });
  }

  override registerGameDeletedObserver(observer: (gameId: number) => void): void {
    this.gameDeletedSubject.subscribe(observer);
    this.rxStompService.watch(`/topic/games/deleted`).subscribe((message: IMessage) => {
      let deletedGameId: number = Number.parseInt(message.body);
      this.gameDeletedSubject.next(deletedGameId);
    });
  }

  override registerGameUpdatedObserver(gameId: number, observer: (updatedGame: GameResponseDto) => void) {
    if(!this.gameUpdatedSubjects.has(gameId)){
      this.gameUpdatedSubjects.set(gameId, new Subject<GameResponseDto>());
      this.rxStompService.watch(`/topic/games/${gameId}`).subscribe((message: IMessage) => {
        let updatedGame = JSON.parse(message.body);
        this.gameUpdatedSubjects.get(gameId)!.next(updatedGame);
      });
    }
    this.gameUpdatedSubjects.get(gameId)!.subscribe(observer)
  }

  registerGameLogsObserver(gameId: number, observer: (gameLog: GameLog) => void) {
    if(!this.gameLogsAddedSubjects.has(gameId)){
      this.gameLogsAddedSubjects.set(gameId, new Subject<GameLog>());
      this.rxStompService.watch(`/topic/games/${gameId}/logs`).subscribe((message: IMessage) => {
        let updatedGame = JSON.parse(message.body);
        this.gameLogsAddedSubjects.get(gameId)!.next(updatedGame);
      });
    }
    this.gameLogsAddedSubjects.get(gameId)!.subscribe(observer)
  }

  override getGame(gameId: number): Observable<GameResponseDto> {
    return this.service.getGameById(gameId);
  }

  override playTurn(gameId: number, userId: number): Observable<GameResponseDto> {
    return this.service.playTurn(gameId,userId);
  }

  override getGames(): Observable<Array<GameResponseDto>> {
    return this.service.listGames()
      .pipe(
        catchError(this.handleError("getGames", [])),
        // map(value => {
        //   return value.map(dto => new Game(dto.id, dto.name, dto.state));
        // })
      )
  }

  override start(gameId: number, userId: number): Observable<GameResponseDto> {
    return this.service.startGame(gameId, userId);
  }

  override join(game: GameResponseDto, user: UserDto): Observable<GameResponseDto> {
    return this.service.addPlayerToGame(game.id, user.id, user);
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
