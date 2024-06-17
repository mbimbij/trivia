package com.adaptionsoft.games.trivia.domain;

import java.util.Map;

public record Question(
        String id,
        String questionText,
        Map<AnswerCode, String> availableAnswers,
        AnswerCode correctAnswer
) {
}
