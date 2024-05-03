import {Injectable} from '@angular/core';
import {GameServiceAbstract} from "./game-service-abstract";
import {Game} from "./game";
import {catchError, map, Observable, of, tap} from "rxjs";
import {GameResponseDto, TriviaControllerService, UserDto} from "./openapi-generated";
import {HttpClient} from "@angular/common/http";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {mockGame1} from "./test-helpers";

@Injectable({
  providedIn: 'root'
})
export class GameService extends GameServiceAbstract {
  constructor(private service: TriviaControllerService) {
    super();
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
