package com.adaptionsoft.games.uglytrivia;

public class Player {
    private final String name;
    private boolean inPenaltyBox;
    private int coinCount;
    private int location;

    public Player(String name) {
        this.name = name;
        this.location = 0;
        this.coinCount = 0;
        this.inPenaltyBox = false;
    }

    public String getName() {
        return name;
    }

    public void advanceLocation(int roll) {
        this.location = (this.location + roll) % 12;
    }

    public int getLocation() {
        return location;
    }

    public void addCoin() {
        coinCount++;
    }

    public int getCoinCount() {
        return coinCount;
    }

    public boolean isInPenaltyBox() {
        return inPenaltyBox;
    }

    public void goToPenaltyBox() {
        inPenaltyBox = true;
    }
}
