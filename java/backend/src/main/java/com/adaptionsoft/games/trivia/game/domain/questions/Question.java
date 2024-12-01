package com.adaptionsoft.games.trivia.game.domain.questions;

import com.adaptionsoft.games.trivia.game.domain.AnswerCode;
import com.adaptionsoft.games.trivia.game.domain.AvailableAswers;
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
    public boolean isCorrect(AnswerCode answerCode) {
        return correctAnswer() == answerCode;
    }
}
