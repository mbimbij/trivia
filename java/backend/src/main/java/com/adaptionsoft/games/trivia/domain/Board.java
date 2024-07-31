package com.adaptionsoft.games.trivia.domain;

public class Board {
    final int squaresCount;

    public Board(int squaresCount) {
        this.squaresCount = squaresCount;
    }

    int getSquaresCount() {
        return squaresCount;
    }

    int computeNewLocation(Player player, Dice.Roll roll) {
        return (player.getLocation() + roll.value()) % getSquaresCount();
    }
}