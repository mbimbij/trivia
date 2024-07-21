import {getMockGame2, getMockQuestion1} from "../common/test-helpers";

describe('Game', () => {

  const testCases = [
    {currentRoll: undefined, currentQuestion: undefined, expectedCanAnswerQuestion: false},
    {currentRoll: undefined, currentQuestion: getMockQuestion1(), expectedCanAnswerQuestion: false},
    {currentRoll: 3, currentQuestion: undefined, expectedCanAnswerQuestion: true},
    {currentRoll: 3, currentQuestion: getMockQuestion1(), expectedCanAnswerQuestion: false},
  ];

  testCases.forEach(
    (
      {currentRoll, currentQuestion, expectedCanAnswerQuestion},
      tcNum
    ) => {
      it(`GIVEN case number ${tcNum}, then expected result should be ${expectedCanAnswerQuestion}`,
        () => {
          // GIVEN a game with a current roll BUT no current question
          let game = getMockGame2();
          game.currentRoll = currentRoll as number;
          game.currentQuestion = currentQuestion;

          // AND a player not in the penalty box
          let currentPlayer = game.currentPlayer;

          // THEN player can draw a question
          expect(game.canDrawQuestion(currentPlayer)).toEqual(expectedCanAnswerQuestion);
        }
      );
    }
  )
});
