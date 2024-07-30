package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.statemachine.Action;

public enum PlayerAction implements Action {
    ROLL_DICE,
    UPDATE_LOCATION,
    DRAW_QUESTION,
    SUBMIT_ANSWER,
    ANSWER_CORRECTLY,
    ANSWER_INCORRECTLY,
    VALIDATE,
    ROLL_EVEN_NUMBER_IN_PENALTY_BOX,
    ROLL_ODD_NUMBER_IN_PENALTY_BOX,
    END_TURN,
    END_GAME
}
