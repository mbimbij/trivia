package com.adaptionsoft.games.trivia.domain.statemachine;

public record Transition(
        State startState,
        Action action,
        State endState
) {
}
