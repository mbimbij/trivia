package com.adaptionsoft.games.uglytrivia;

public class Board {
    public Board() {
        initializeQuestions();
    }

    void drawQuestion(int playerLocation) {
        System.out.println(getQuestionCategory(playerLocation).drawCard());
    }

    public Questions getQuestionCategory(int playerLocation) {
        int categoriesCount = Questions.values().length;
        return Questions.values()[playerLocation % categoriesCount];
    }

    void initializeQuestions() {
        for (int i = 0; i < 50; i++) {
            Questions.POP.stackCard("Pop Question " + i);
            Questions.SCIENCE.stackCard("Science Question " + i);
            Questions.SPORTS.stackCard("Sports Question " + i);
            Questions.ROCK.stackCard("Rock Question " + i);
        }
    }
}