package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.AnswerCode;
import com.adaptionsoft.games.trivia.domain.Player;
import com.adaptionsoft.games.trivia.domain.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record QuestionDto(
        @NotNull
        String id,
        @NotNull
        String questionText,
        @NotNull
        Map<AnswerCode, String> availableAnswers
) {
    public static QuestionDto from(Question question) {
        if (question == null) return null;
        return new QuestionDto(question.id(),
                question.questionText(),
                question.availableAnswers());
    }
}
