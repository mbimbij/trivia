package com.adaptionsoft.games.uglytrivia;

public class DummyQuestionInitializationStrategy implements QuestionInitializationStrategy {
    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            Questions.POP.stackCard("Pop Question " + i);
            Questions.SCIENCE.stackCard("Science Question " + i);
            Questions.SPORTS.stackCard("Sports Question " + i);
            Questions.ROCK.stackCard("Rock Question " + i);
        }
    }
}
