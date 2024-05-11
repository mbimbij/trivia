import {Injectable} from "@angular/core";
import {GameServiceAbstract} from "./game-service-abstract";
import {Observable, of} from "rxjs";
import {GameResponseDto, UserDto} from "./openapi-generated";
import {mockGame1, mockGame2} from "./test-helpers";

@Injectable({
  providedIn: 'root'
})
export class GameServiceMock extends GameServiceAbstract {
  getGames(): Observable<Array<GameResponseDto>> {
    return of([
      mockGame1,
      mockGame2,
    ]);
  }
  join(game: GameResponseDto, user: UserDto): Observable<GameResponseDto> {
    return of(game);
  }

  override start(gameId: number, userId: number): Observable<GameResponseDto> {
    return of(mockGame1);
  }
}