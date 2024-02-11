package com.adaptionsoft.games.uglytrivia;

import java.util.Arrays;

class DummyQuestionInitializationStrategy implements QuestionInitializationStrategy {

    private static final int QUESTIONS_PER_CATEGORY_COUNT = 50;

    @Override
    public void run() {
        Arrays.stream(Questions.values())
                .forEach(questionsCategory -> {
                    questionsCategory.clear();
                    stackCards(questionsCategory);
                });
    }

    private void stackCards(Questions questionsCategory) {
        String categoryName = questionsCategory.toString();
        for (int i = 0; i < QUESTIONS_PER_CATEGORY_COUNT; i++) {
            questionsCategory.stackCard("%s Question %s".formatted(categoryName, i));
        }
    }
}
