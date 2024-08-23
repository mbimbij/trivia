import {Observable} from "rxjs";
import {GameLog} from "../openapi-generated/gamelogs";

export abstract class GameLogsServiceAbstract {
  abstract initGameLogs(gameId: number): void;
  abstract getGameLogs(gameId: number): Observable<Array<GameLog>>;
}

