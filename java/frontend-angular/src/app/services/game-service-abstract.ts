import {Observable} from "rxjs";
import {GameLog, GameResponseDto, QuestionDto} from "../openapi-generated";
import {User} from "../user/user";
import {Game} from "../game/game";

export abstract class GameServiceAbstract {
  abstract initGamesList(): void;

  abstract getGames(): Observable<Array<Game>>;

  abstract getGame(gameId: number): Observable<Game>;

  abstract create(name: string, user: User): Observable<Game>;

  abstract join(game: Game, user: User): Observable<Game>;

  abstract start(gameId: number, userId: string): Observable<Game>;

  abstract rollDice(gameId: number, userId: string): Observable<Game>;

  abstract drawQuestion(gameId: number, userId: string): Observable<QuestionDto>;

  abstract answerQuestion(gameId: number, userId: string, answerCode: string): Observable<boolean>;

  abstract delete(gameId: number): Observable<void> ;

  abstract initGameLogs(gameId: number): void;

  abstract getGameLogs(gameId: number): Observable<Array<GameLog>>;

}

