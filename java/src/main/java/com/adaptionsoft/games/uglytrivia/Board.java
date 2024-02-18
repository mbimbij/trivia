package com.adaptionsoft.games.uglytrivia;

class Board {

    String drawQuestion(int playerLocation) {
        return getQuestionCategory(playerLocation).drawCard();
    }

    public QuestionCategory getQuestionCategory(int playerLocation) {
        int categoriesCount = QuestionCategory.values().length;
        return QuestionCategory.values()[playerLocation % categoriesCount];
    }

}