package com.adaptionsoft.games.uglytrivia;

public class FixedAnsweringStrategy implements AnsweringStrategy {

    private final boolean fixedResponse;

    public FixedAnsweringStrategy(boolean fixedResponse) {
        this.fixedResponse = fixedResponse;
    }

    @Override
    public boolean isAnsweringCorrectly() {
        return fixedResponse;
    }
}
