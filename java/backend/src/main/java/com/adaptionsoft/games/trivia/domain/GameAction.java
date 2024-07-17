package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.statemachine.Action;

public enum GameAction implements Action {
    JOIN, START, END_TURN, END_GAME
}
