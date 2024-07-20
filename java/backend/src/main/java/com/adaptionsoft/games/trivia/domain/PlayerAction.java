package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.statemachine.Action;

public enum PlayerAction implements Action {
    ROLL_DICE,
    UPDATE_LOCATION,
    DRAW_QUESTION,
    SUBMIT_ANSWER,
    ANSWER_CORRECTLY,
    ANSWER_INCORRECTLY,
    GET_OUT_OF_PENALTY_BOX,
    STAY_IN_PENALTY_BOX,
    END_TURN,
    END_GAME
}
