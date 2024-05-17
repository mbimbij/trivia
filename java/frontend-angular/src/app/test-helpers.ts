import {UserDto} from "./openapi-generated";

export const mockGame1 = {
  id: 1,
  name: "game1",
  state: "created",
  turn: 0,
  creator: {id: 1, name: "player1"},
  currentPlayer: {id: 1, name: "player1"},
  players: [{id: 1, name: "player1"}, {id: 2, name: "player2"}]
};

export const mockGame2 = {
  id: 2,
  name: "game2",
  state: "started",
  turn: 2,
  creator: {id: 2, name: "player2"},
  currentPlayer: {id: 2, name: "player2"},
  players: [{id: 2, name: "player2"}, {id: 3, name: "player3"}, {id: 4, name: "player"}]
};

export const mockUser1: UserDto = {
  id: 1,
  name: "user1",
  coinCount: 2
}
