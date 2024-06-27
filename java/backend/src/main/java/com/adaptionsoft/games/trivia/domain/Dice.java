package com.adaptionsoft.games.trivia.domain;

import java.util.Random;

public class Dice {
    private final Random rand;

    public Dice(Random rand) {
        this.rand = rand;
    }

    Roll roll() {
        return new Roll(rand.nextInt(6) + 1);
    }

    public record Roll(int value) {
        boolean isPair() {
            return value() % 2 == 0;
        }
    }
}