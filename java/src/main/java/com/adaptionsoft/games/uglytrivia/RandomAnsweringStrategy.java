package com.adaptionsoft.games.uglytrivia;

import java.util.Random;

public class RandomAnsweringStrategy implements AnsweringStrategy {
    private final Random rand;


    public RandomAnsweringStrategy() {
        this.rand = new Random();
    }

    public RandomAnsweringStrategy(Random rand) {
        this.rand = rand;
    }

    @Override
    public boolean isAnsweringCorrectly() {
        return rand.nextInt(9) != 7;
    }
}