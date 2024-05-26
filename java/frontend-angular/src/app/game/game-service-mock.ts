import {Injectable} from "@angular/core";
import {GameServiceAbstract} from "../services/game-service-abstract";
import {Observable, of} from "rxjs";
import {GameLog} from "../openapi-generated";
import {mockGame1, mockGame2} from "../common/test-helpers";
import {User} from "../user/user";
import {Game} from "./game";

@Injectable({
  providedIn: 'root'
})
export class GameServiceMock extends GameServiceAbstract  {
  override doInit(): void {

  }
  override create(name: string, user: User): Observable<Game> {
    return of(mockGame1);
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

  override getGame(gameId: number): Observable<Game> {
    return of(mockGame1);
  }

  override playTurn(gameId: number, userId: string): Observable<Game> {
    return of(mockGame1);
  }

  getGames(): Observable<Array<Game>> {
    return of([
      mockGame1,
      mockGame2,
    ]);
  }

  join(game: Game, user: User): Observable<Game> {
    return of(game);
  }

  override start(gameId: number, userId: string): Observable<Game> {
    return of(mockGame1);
  }
}
