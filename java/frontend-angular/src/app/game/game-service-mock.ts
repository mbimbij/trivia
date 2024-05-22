import {Injectable} from "@angular/core";
import {GameServiceAbstract} from "./game-service-abstract";
import {Observable, of} from "rxjs";
import {GameLog, GameResponseDto, UserDto} from "../openapi-generated";
import {mockGame1, mockGame2} from "../common/test-helpers";
import {User} from "../user/user";

@Injectable({
  providedIn: 'root'
})
export class GameServiceMock extends GameServiceAbstract {
  override create(name: string, user: User): Observable<GameResponseDto> {
    return of(mockGame1);
  }

  override registerGameCreatedObserver(observer: (newGame: GameResponseDto) => void): void {
  }

  override registerGameDeletedObserver(observer: (gameId: number) => void): void {
  }

  override delete(gameId: number): Observable<void> {
    return of();
  }

  override registerGameLogsObserver(gameId: number, observer: (updatedGame: GameLog) => void): void {
  }

  override getGameLogs(gameId: number): Observable<Array<GameLog>> {
    return of([
      {gameId: gameId, value: "log 1"},
      {gameId: gameId, value: "log 2"},
    ]);
  }

  override registerGameUpdatedObserver(gameId: number, observer: (updatedGame: GameResponseDto) => void): void {
  }

  override getGame(gameId: number): Observable<GameResponseDto> {
    return of(mockGame1);
  }

  override playTurn(gameId: number, userId: number): Observable<GameResponseDto> {
    return of(mockGame1);
  }

  getGames(): Observable<Array<GameResponseDto>> {
    return of([
      mockGame1,
      mockGame2,
    ]);
  }

  join(game: GameResponseDto, user: User): Observable<GameResponseDto> {
    return of(game);
  }

  override start(gameId: number, userId: number): Observable<GameResponseDto> {
    return of(mockGame1);
  }
}
