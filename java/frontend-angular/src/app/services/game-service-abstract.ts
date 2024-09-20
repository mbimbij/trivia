import {Observable} from "rxjs";
import {User} from "../user/user";
import {Game} from "../game/game";
import {AnswerCode, AnswerDto, QuestionDto} from "../openapi-generated/game";

export abstract class GameServiceAbstract {
  abstract initGamesList(): void;

  abstract getGames(): Observable<Array<Game>>;

  abstract getGame(gameId: number): Observable<Game>;

  abstract create(name: string, user: User): Observable<Game>;

  abstract join(gameId: number, user: User): Observable<Game>;

  abstract start(gameId: number, userId: string): Observable<Game>;

  abstract rollDice(gameId: number, userId: string): Observable<Game>;

  abstract drawQuestion(gameId: number, userId: string): Observable<QuestionDto>;

  abstract answerQuestion(gameId: number, userId: string, answerCode: AnswerCode): Observable<AnswerDto>;

  abstract validate(gameId: number, userId: string): Observable<void>;

  abstract delete(gameId: number): Observable<void> ;

}

