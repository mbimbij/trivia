package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.statemachine.State;

public enum PlayerState implements State {
    WAITING_FOR_DICE_ROLL,
    WAITING_FOR_ROLL_DICE_EVALUATION,
    IN_PENALTY_BOX,
    WAITING_TO_UPDATE_LOCATION,
    WAITING_TO_DRAW_1ST_QUESTION,
    WAITING_FOR_1ST_ANSWER,
    WAITING_FOR_1ST_ANSWER_EVALUATION,
    WAITING_TO_DRAW_2ND_QUESTION,
    WAITING_FOR_2ND_ANSWER,
    WAITING_FOR_2ND_ANSWER_EVALUATION,
    WAITING_TO_END_TURN_OR_GAME ,
    GAME_END
}
