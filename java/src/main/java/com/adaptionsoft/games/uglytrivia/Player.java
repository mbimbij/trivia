package com.adaptionsoft.games.uglytrivia;

public class Player {
    private final String name;
    private int location;

    public Player(String name) {
        this.name = name;
        this.location = 0;
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
}
