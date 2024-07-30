import {User} from "../user/user";
import {Observable, of} from "rxjs";
import {Game} from "../game/game";
import {playerDtoToPlayer, userToPlayer} from "./helpers";
import {QuestionDto} from "../openapi-generated";

export const mockUser1: User = getMockUser1()

export const mockUser2: User = getMockUser2()

export const mockPlayer1 = getMockPlayer1()

export const mockPlayer2 = getMockPlayer2();

export const mockPlayer3 = playerDtoToPlayer(
  {id: "3", name: "player3", coinCount: 1, isInPenaltyBox: false, consecutiveIncorrectAnswersCount: 0, state: "WAITING_FOR_DICE_ROLL"}
);

export const mockPlayer4 = playerDtoToPlayer(
  {id: "4", name: "player4", coinCount: 1, isInPenaltyBox: false, consecutiveIncorrectAnswersCount: 0, state: "WAITING_FOR_DICE_ROLL"}
);

export const mockGame1: Game = getMockGame1();

export const mockGame2: Game = getMockGame2();

export function getMockQuestion1() {
  return {
    id: "question-1", questionText: "question 1", availableAnswers: {
      "A": "answer A",
      "B": "answer B",
      "C": "answer C",
      "D": "answer D"
    }
  };
}

export const mockQuestion1: QuestionDto = getMockQuestion1()

export function getMockUser1() {
  return {
    id: "1",
    name: "user1",
    isAnonymous: false
  };
}

export function getMockUser2() {
  return {
    id: "2",
    name: "user2",
    isAnonymous: true
  };
}

export function getMockPlayer1() {
  return userToPlayer(mockUser1);
}

export function getMockPlayer2() {
  return userToPlayer(mockUser2);
}

export function getMockGame1() {
  return new Game(1,
    "game1",
    "created",
    0,
    mockPlayer1,
    mockPlayer1,
    [mockPlayer1, mockPlayer2]
  );

}

export function getMockGame2() {
  return new Game(2,
    "game2",
    "started",
    0,
    mockPlayer2,
    mockPlayer2,
    [mockPlayer2, mockPlayer3, mockPlayer4],
    undefined,
    getMockQuestion1(),
    3
  );
}

export function getMockGame3() {
  return new Game(2,
    "game2",
    "started",
    0,
    mockPlayer2,
    mockPlayer1,
    [mockPlayer2, mockPlayer3, mockPlayer4],
    undefined,
    getMockQuestion1(),
    3
  );
}

export class MockActivatedRoute {
  params: Observable<any> = of({id: '1'}); // Mock route params
}

