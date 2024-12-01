package com.adaptionsoft.games.trivia.game.domain;

public record Answer(
        boolean isCorrect,
        String explanations
) {
}
