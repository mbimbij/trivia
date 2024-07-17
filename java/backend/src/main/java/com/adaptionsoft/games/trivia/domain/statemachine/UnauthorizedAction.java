package com.adaptionsoft.games.trivia.domain.statemachine;

public class UnauthorizedAction extends RuntimeException {
    public UnauthorizedAction(String action) {
        super("action %s is not authorized".formatted(action));
    }
}
