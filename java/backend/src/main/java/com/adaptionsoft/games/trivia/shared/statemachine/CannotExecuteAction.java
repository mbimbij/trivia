package com.adaptionsoft.games.trivia.shared.statemachine;

import com.adaptionsoft.games.trivia.game.domain.exception.BusinessException;

public class CannotExecuteAction extends BusinessException {
    public CannotExecuteAction(String entityIdentifier, Action action, State currentState) {
        super("%s - cannot execute action %s in state %s".formatted(entityIdentifier, action, currentState));
    }
}
