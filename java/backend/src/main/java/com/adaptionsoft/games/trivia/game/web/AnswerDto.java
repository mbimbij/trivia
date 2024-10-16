package com.adaptionsoft.games.trivia.game.web;

import com.adaptionsoft.games.trivia.game.domain.Answer;

public record AnswerDto(
        boolean isCorrect,
        String explanations
) {
    public static AnswerDto from(Answer answer){
        return new AnswerDto(answer.isCorrect(), answer.explanations());
    }
}
