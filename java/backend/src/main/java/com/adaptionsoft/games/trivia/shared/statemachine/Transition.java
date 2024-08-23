package com.adaptionsoft.games.trivia.shared.statemachine;

public record Transition(
        State startState,
        Action action,
        State endState
) {
}
