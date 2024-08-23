import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, of} from "rxjs";
import {IMessage} from "@stomp/rx-stomp";
import {RxStompService} from "../adapters/websockets/rx-stomp.service";
import {GameLog, GameLogsControllerService} from "../openapi-generated/gamelogs";
import {GameLogsServiceAbstract} from "../services/gamelogs-service-abstract";

@Injectable({
  providedIn: 'root'
})
export class GameLogsService extends GameLogsServiceAbstract {
  private gameLogsSubject = new BehaviorSubject<GameLog[]>([])
  private isGameLogsHandlerRegistered = new Set<number>()

  constructor(private gameLogsOpenApiService: GameLogsControllerService,
              private rxStompService: RxStompService) {
    super();
  }

  initGameLogs(gameId: number) {
    this.gameLogsOpenApiService.getGameLogs(gameId)
      .subscribe(gameLogs => {
        this.gameLogsSubject.next(this.splitLogs(gameLogs));
      })
    this.registerNewGameLogHandler(gameId);
  }

  splitLogs(gameLogs: GameLog[]): GameLog[] {
    return gameLogs.flatMap(
      (g) => g.value.split('\n')
        .filter(text => text.trim() != "")
        .map(text => buildGameLog(text, g)))
      ;

    function buildGameLog(text: string, g: GameLog) {
      return {
        value: text,
        gameId: g.gameId
      } as GameLog;
    }
  }

  private registerNewGameLogHandler(gameId: number) {
    if (!this.isGameLogsHandlerRegistered.has(gameId)) {
      this.isGameLogsHandlerRegistered.add(gameId)
      this.rxStompService.watch(`/topic/games/${gameId}/logs`)
        .subscribe((message: IMessage) => {
          let newGameLog = JSON.parse(message.body) as GameLog;
          this.handleNewGameLog(newGameLog);
        });
    }
  }

  public handleNewGameLog(newGameLog: GameLog) {
    let logsToAdd = newGameLog.value
      .split('\n')
      .filter(value => value.trim())
      .map((m) => {
        return {gameId: 1, value: m} as GameLog;
      });
    this.gameLogsSubject.next(this.gameLogsSubject.value.concat(logsToAdd))
  }

  override getGameLogs(gameId: number): Observable<Array<GameLog>> {
    return this.gameLogsSubject.asObservable();
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
