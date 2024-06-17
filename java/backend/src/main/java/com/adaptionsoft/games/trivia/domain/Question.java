package com.adaptionsoft.games.trivia.domain;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record Question(
        @NotNull
        String id,
        @NotNull
        String questionText,
        @NotNull
        Map<AnswerCode, String> availableAnswers,
        @NotNull
        AnswerCode correctAnswer
) {
    boolean isCorrect(AnswerCode answerCode) {
        return correctAnswer() == answerCode;
    }
}
