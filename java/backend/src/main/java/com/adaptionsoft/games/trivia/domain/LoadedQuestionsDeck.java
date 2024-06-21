package com.adaptionsoft.games.trivia.domain;

public class LoadedQuestionsDeck extends QuestionsDeck {
    private final Question question;

    public LoadedQuestionsDeck(Question question) {
        super(null);
        this.question = question;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    Question drawQuestion(int playerLocation) {
        return question;
    }
}
