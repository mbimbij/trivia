import {Observable} from "rxjs";
import {GameLog, GameResponseDto} from "./openapi-generated";
import {User} from "./user";

export abstract class GameServiceAbstract {
  abstract getGames(): Observable<Array<GameResponseDto>>;

  abstract getGame(gameId: number): Observable<GameResponseDto>;

  abstract create(name: string, user: User): Observable<GameResponseDto>;

  abstract join(game: GameResponseDto, user: User): Observable<GameResponseDto>;

  abstract start(gameId: number, userId: number): Observable<GameResponseDto>;

  abstract playTurn(gameId: number, userId: number): Observable<GameResponseDto>;

  abstract registerGameCreatedObserver(observer: (newGame: GameResponseDto) => void): void;

  abstract registerGameDeletedObserver(observer: (gameId: number) => void): void;

  abstract registerGameUpdatedObserver(gameId: number, observer: (updatedGame: GameResponseDto) => void): void;

  abstract registerGameLogsObserver(gameId: number, observer: (updatedGame: GameLog) => void): void;

  abstract getGameLogs(gameId: number): Observable<Array<GameLog>>;

  abstract delete(gameId: number): Observable<void> ;
}

