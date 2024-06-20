package com.adaptionsoft.games.trivia.domain;

public enum State {
    CREATED("created"),
    STARTED("started"),
    ENDED("ended"),
    ;

    private final String value;

    State(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
