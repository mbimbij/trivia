package com.adaptionsoft.games.trivia.game.domain;

import com.adaptionsoft.games.trivia.game.domain.questions.Question;
import com.adaptionsoft.games.trivia.game.domain.questions.QuestionsDeck;

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
    public Question drawQuestion(int playerLocation) {
        return question;
    }
}
