import {User} from "../user/user";
import {Observable, of} from "rxjs";
import {GameResponseDto} from "../openapi-generated";

export const mockGame1: GameResponseDto = {
  id: 1,
  name: "game1",
  state: "created",
  turn: 0,
  creator: {id: "1", name: "player1", coinCount: 0},
  currentPlayer: {id: "1", name: "player1", coinCount: 0},
  players: [{id: "1", name: "player1", coinCount: 0}, {id: "2", name: "player2", coinCount: 1}]
};

export const mockGame2: GameResponseDto = {
  id: 2,
  name: "game2",
  state: "started",
  turn: 2,
  creator: {id: "2", name: "player2", coinCount: 0},
  currentPlayer: {id: "2", name: "player2", coinCount: 0},
  players: [{id: "2", name: "player2", coinCount: 0}, {id: "3", name: "player3", coinCount: 1}, {
    id: "4",
    name: "player",
    coinCount: 2
  }]
};

export const mockUser1: User = {
  id: "1",
  name: "user1",
  isAnonymous: false
}

export class MockActivatedRoute {
  params: Observable<any> = of({id: '123'}); // Mock route params
}
