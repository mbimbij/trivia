import {Observable, of} from "rxjs";
import {Game} from "../game/game";
import {playerDtoToPlayer, userToPlayer} from "./helpers";

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
  return userToPlayer(getMockUser1());
}

export function getMockPlayer2() {
  return userToPlayer(getMockUser2());
}

export function getMockPlayer3() {
  return playerDtoToPlayer(
    {
      id: "3",
      name: "player3",
      coinCount: 1,
      isInPenaltyBox: false,
      consecutiveIncorrectAnswersCount: 0,
      state: "WAITING_FOR_DICE_ROLL"
    }
  );
}

export function getMockPlayer4() {
  return playerDtoToPlayer(
    {
      id: "4",
      name: "player4",
      coinCount: 1,
      isInPenaltyBox: false,
      consecutiveIncorrectAnswersCount: 0,
      state: "WAITING_FOR_DICE_ROLL"
    }
  );
}

export function getMockGame1() {
  return new Game(1,
    "game1",
    "created",
    0,
    getMockPlayer1(),
    getMockPlayer1(),
    [getMockPlayer1(), getMockPlayer2()]
  );

}

export function getMockGame2() {
  return new Game(2,
    "game2",
    "started",
    0,
    getMockPlayer2(),
    getMockPlayer2(),
    [getMockPlayer2(), getMockPlayer3(), getMockPlayer4()],
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
    getMockPlayer2(),
    getMockPlayer1(),
    [getMockPlayer2(), getMockPlayer3(), getMockPlayer4()],
    undefined,
    getMockQuestion1(),
    3
  );
}

export class MockActivatedRoute {
  params: Observable<any> = of({id: '1'}); // Mock route params
}

