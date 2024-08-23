package com.adaptionsoft.games.trivia.game.domain;

import com.adaptionsoft.games.trivia.shared.statemachine.Action;

public enum GameAction implements Action {
    JOIN, START, END_TURN, END_GAME
}
