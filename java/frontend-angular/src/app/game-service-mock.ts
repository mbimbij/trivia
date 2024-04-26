import {Injectable} from "@angular/core";
import {GameServiceAbstract} from "./game-service-abstract";
import {Observable, of} from "rxjs";
import {Game} from "./game";
import {GameResponseDto} from "./openapi-generated";

@Injectable({
  providedIn: 'root'
})
export class GameServiceMock extends GameServiceAbstract {
  getGames(): Observable<Array<GameResponseDto>> {
    return of([
      {
        id: 1,
        name: "game1",
        state: "created",
        creator: {id: 1, name: "player1"},
        currentPlayer: {id: 1, name: "player1"},
        players: [{id: 1, name: "player1"}, {id: 2, name: "player2"}]
      },
      {
        id: 2,
        name: "game2",
        state: "started",
        creator: {id: 2, name: "player2"},
        currentPlayer: {id: 2, name: "player2"},
        players: [{id: 2, name: "player2"}, {id: 3, name: "player3"}, {id: 4, name: "player"}]
      },
    ]);
  }
}
