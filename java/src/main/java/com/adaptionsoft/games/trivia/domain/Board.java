package com.adaptionsoft.games.trivia.domain;

class Board {

    private final int squaresCount;

    private final Questions questions;

    Board(int squaresCount, Questions questions) {
        this.squaresCount = squaresCount;
        this.questions = questions;
    }

    String drawQuestion(int playerLocation) {
        return questions.drawQuestion(getQuestionCategory(playerLocation));
    }

    Questions.Category getQuestionCategory(int playerLocation) {
        int categoriesCount = Questions.Category.values().length;
        return Questions.Category.values()[playerLocation % categoriesCount];
    }

    int getSquaresCount() {
        return squaresCount;
    }
}