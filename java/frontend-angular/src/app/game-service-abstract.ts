import {Observable} from "rxjs";
import {GameResponseDto, UserDto} from "./openapi-generated";

export abstract class GameServiceAbstract {
  abstract getGames(): Observable<Array<GameResponseDto>>;
  abstract getGame(gameId: number): Observable<GameResponseDto>;
  abstract join(game: GameResponseDto, user: UserDto): Observable<GameResponseDto>;
  abstract start(gameId: number, userId: number): Observable<GameResponseDto>;
  abstract playTurn(gameId: number, userId: number): Observable<GameResponseDto>;
  abstract registerGameUpdatedObserver(gameId: number, observer: (updatedGame: GameResponseDto) => void): void;
}

