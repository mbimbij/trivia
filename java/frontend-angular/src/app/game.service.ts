import {Injectable} from '@angular/core';
import {GameServiceAbstract} from "./game-service-abstract";
import {catchError, Observable, of, Subject} from "rxjs";
import {GameResponseDto, TriviaControllerService, UserDto} from "./openapi-generated";
import {IMessage} from "@stomp/rx-stomp";
import {RxStompService} from "./rx-stomp.service";

@Injectable({
  providedIn: 'root'
})
export class GameService extends GameServiceAbstract {
  private gameUpdatedSubjects = new Map<number, Subject<GameResponseDto>>()

  constructor(private service: TriviaControllerService,
              private rxStompService: RxStompService) {
    super();
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
