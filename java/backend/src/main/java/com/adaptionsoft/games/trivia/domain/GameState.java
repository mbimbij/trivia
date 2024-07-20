package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.statemachine.State;

public enum GameState implements State {
    CREATED("created"),
    STARTED("started"),
    ENDED("ended"),
    ;

    private final String value;

    GameState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
