package com.adaptionsoft.games.trivia.game.web;

import com.adaptionsoft.games.trivia.game.domain.AvailableAswers;
import com.adaptionsoft.games.trivia.game.domain.Question;
import jakarta.validation.constraints.NotNull;

public record QuestionDto(
        @NotNull
        String id,
        @NotNull
        String questionText,
        @NotNull
        AvailableAswers availableAnswers
) {
    public static QuestionDto from(Question question) {
        if (question == null) return null;
        return new QuestionDto(question.id(),
                question.questionText(),
                question.availableAnswers());
    }
}
