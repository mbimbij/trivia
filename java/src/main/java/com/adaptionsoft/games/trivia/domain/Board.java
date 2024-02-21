package com.adaptionsoft.games.trivia.domain;

class Board {

    private final int squaresCount;

    public Board(int squaresCount) {
        this.squaresCount = squaresCount;
    }

    String drawQuestion(int playerLocation) {
        return getQuestionCategory(playerLocation).drawCard();
    }

    public QuestionCategory getQuestionCategory(int playerLocation) {
        int categoriesCount = QuestionCategory.values().length;
        return QuestionCategory.values()[playerLocation % categoriesCount];
    }

    int getSquaresCount() {
        return squaresCount;
    }
}