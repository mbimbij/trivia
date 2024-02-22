package com.adaptionsoft.games.trivia.domain;

import java.util.Random;

class RandomAnsweringStrategy implements AnsweringStrategy {
    private final Random rand;

    public RandomAnsweringStrategy(Random rand) {
        this.rand = rand;
    }

    @Override
    public boolean isAnsweringCorrectly() {
        return rand.nextInt(9) != 7;
    }
}