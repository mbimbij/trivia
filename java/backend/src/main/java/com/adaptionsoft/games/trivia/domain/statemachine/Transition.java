package com.adaptionsoft.games.trivia.domain.statemachine;

public record Transition(String startState, String endState, String action) {
}
