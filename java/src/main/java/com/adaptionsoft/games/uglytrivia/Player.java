package com.adaptionsoft.games.uglytrivia;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {
    @EqualsAndHashCode.Include
    private final String name;
    private final AnsweringStrategy answeringStrategy;
    private boolean isInPenaltyBox;
    private int coinCount;
    private int location;

    public Player(String name, AnsweringStrategy answeringStrategy) {
        this.name = name;
        this.location = 0;
        this.coinCount = 0;
        this.answeringStrategy = answeringStrategy;
    }

    public void advanceLocation(int roll) {
        this.location = (this.location + roll) % 12;
    }

    public void addCoin() {
        coinCount++;
    }


    public void goToPenaltyBox() {
        isInPenaltyBox = true;
    }

    /**
     * Only used for testing the feature "go to jail after 2 consecutive wrong answers"
     *
     * @return
     */
    public boolean isAnsweringCorrectly() {
        return answeringStrategy.isAnsweringCorrectly();
    }
}
