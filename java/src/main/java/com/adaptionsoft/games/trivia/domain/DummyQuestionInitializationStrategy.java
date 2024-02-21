package com.adaptionsoft.games.trivia.domain;

import java.util.Arrays;

class DummyQuestionInitializationStrategy implements QuestionInitializationStrategy {

    private static final int QUESTIONS_PER_CATEGORY_COUNT = 50;

    @Override
    public void run() {
        QuestionCategory.clearDeck();
        Arrays.stream(QuestionCategory.values())
                .forEach(this::stackCards);
    }

    private void stackCards(QuestionCategory questionsCategory) {
        String categoryName = questionsCategory.toString();
        for (int i = 0; i < QUESTIONS_PER_CATEGORY_COUNT; i++) {
            questionsCategory.stackCard("%s Question %s".formatted(categoryName, i));
        }
    }
}
