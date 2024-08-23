package com.adaptionsoft.games.trivia.game.domain;

import jakarta.validation.constraints.NotNull;

public record Question(
        @NotNull
        String id,
        @NotNull
        String questionText,
        @NotNull
        AvailableAswers availableAnswers,
        @NotNull
        AnswerCode correctAnswer,
//        @NotNull
        String explanations
) {
    boolean isCorrect(AnswerCode answerCode) {
        return correctAnswer() == answerCode;
    }
}
