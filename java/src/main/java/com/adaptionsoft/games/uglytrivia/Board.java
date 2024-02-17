package com.adaptionsoft.games.uglytrivia;

class Board {

    String drawQuestion(int playerLocation) {
        return getQuestionCategory(playerLocation).drawCard();
    }

    public Questions getQuestionCategory(int playerLocation) {
        int categoriesCount = Questions.values().length;
        return Questions.values()[playerLocation % categoriesCount];
    }

}