package com.adaptionsoft.games.trivia.game.domain;

import java.util.Collections;

public class LoadedQuestionsDeck extends QuestionsDeck {
    private final Question question;

    public LoadedQuestionsDeck(Question question) {
        super(Collections.emptyMap());
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
