package com.adaptionsoft.games.trivia.domain;

public record Answer(
        boolean isCorrect,
        String explanations
) {
}
