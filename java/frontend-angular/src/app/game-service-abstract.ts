import {Observable} from "rxjs";
import {GameResponseDto, UserDto} from "./openapi-generated";

export abstract class GameServiceAbstract {
  abstract getGames(): Observable<Array<GameResponseDto>>;
  abstract join(game: GameResponseDto, user: UserDto): Observable<GameResponseDto>;
  abstract start(gameId: number, userId: number): Observable<GameResponseDto>;
}

