package com.adaptionsoft.games.trivia.game.domain;

public class LoadedDice extends Dice {
    private final int value;

    public LoadedDice(int value) {
        super(null);
        this.value = value;
    }

    @Override
    Roll roll() {
        return new Roll(value);
    }
}
