package com.adaptionsoft.games.uglytrivia;

class Board {

    void drawQuestion(int playerLocation) {
        System.out.println(getQuestionCategory(playerLocation).drawCard());
    }

    public Questions getQuestionCategory(int playerLocation) {
        int categoriesCount = Questions.values().length;
        return Questions.values()[playerLocation % categoriesCount];
    }

}