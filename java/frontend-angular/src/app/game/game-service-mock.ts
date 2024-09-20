import {Injectable} from "@angular/core";
import {GameServiceAbstract} from "../services/game-service-abstract";
import {BehaviorSubject, Observable, of} from "rxjs";
import {getMockGame1, getMockGame2} from "../common/test-helpers";
import {User} from "../user/user";
import {Game} from "./game";
import {AnswerCode, AnswerDto, QuestionDto} from "../openapi-generated/game";

@Injectable({
  providedIn: 'root'
})
export class GameServiceMock extends GameServiceAbstract {
  override validate(gameId: number, userId: string): Observable<void> {
      throw new Error("Method not implemented.");
  }
  private gameSubject = new BehaviorSubject(getMockGame1());

  override rollDice(gameId: number, userId: string): Observable<Game> {
      throw new Error("Method not implemented.");
  }
  override drawQuestion(gameId: number, userId: string): Observable<QuestionDto> {
      throw new Error("Method not implemented.");
  }
  override answerQuestion(gameId: number, userId: string, answerCode: AnswerCode): Observable<AnswerDto> {
      throw new Error("Method not implemented.");
  }
  override initGamesList(): void {
  }
  override create(name: string, user: User): Observable<Game> {
    return this.gameSubject.asObservable();
  }

  override delete(gameId: number): Observable<void> {
    return of();
  }

  override getGame(gameId: number): Observable<Game> {
    return this.gameSubject.asObservable();
  }

  setGame(game: Game){
    this.gameSubject.next(game);
  }

  getGames(): Observable<Array<Game>> {
    return of([
      getMockGame1(),
      getMockGame2(),
    ]);
  }

  join(gameId: number, user: User): Observable<Game> {
    return of(getMockGame1());
  }

  override start(gameId: number, userId: string): Observable<Game> {
    return of(getMockGame1());
  }
}
