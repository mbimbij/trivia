package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@Getter
public class Player {
    @EqualsAndHashCode.Include
    private final String name;
    private final AnsweringStrategy answeringStrategy;
    private boolean isInPenaltyBox;
    @With // for testing purposes only
    private int coinCount;
    private int location;
    private final List<Event> uncommittedEvents = new ArrayList<>();
    private int turn = 1;
    @Setter // for testing purposes only
    private int consecutiveCorrectAnswersCount;
    private final Board board;
    private final Random rand;

    public Player(String name, AnsweringStrategy answeringStrategy, Board board, Random rand) {
        this.name = name;
        this.answeringStrategy = answeringStrategy;
        this.board = board;
        this.rand = rand;
    }

    public void advanceLocation(int roll) {
        this.location = (this.location + roll) % board.getSquaresCount();
    }

    public void addCoin() {
        coinCount++;
    }


    public void goToPenaltyBox() {
        isInPenaltyBox = true;
    }

    void answerCorrectly() {
        if (isOnACorrectAnswersStreak()) {
            addCoin();
        }

        addCoin();
        raise(new PlayerAnsweredCorrectlyEvent(this),
                new CoinAddedToPlayerEvent(this)
        );
        consecutiveCorrectAnswersCount++;
    }

    void answerIncorrectly() {
        raise(new PlayerAnsweredIncorrectlyEvent(this));
        if(!isOnACorrectAnswersStreak()){
            goToPenaltyBox();
            raise(new PlayerSentToPenaltyBoxEvent(this));
        }
        consecutiveCorrectAnswersCount = 0;
    }

    private void raise(Event... events) {
        uncommittedEvents.addAll(Arrays.asList(events));
    }

    public void incrementTurn() {
        turn++;
    }


    public boolean isOnACorrectAnswersStreak() {
        return consecutiveCorrectAnswersCount >= 3;
    }

    void drawQuestion() {
        String question = board.drawQuestion(location);
        raise(new QuestionAskedToPlayerEvent(this, question));
    }

    boolean isWinning() {
        return (coinCount >= 12);
    }

    boolean isAnsweringCorrectly() {
        return answeringStrategy.isAnsweringCorrectly();
    }

    void answerQuestion() {
        if (isAnsweringCorrectly()) {
            answerCorrectly();
        } else {
            answerIncorrectly();
        }
    }

    List<Event> getUncommittedEventsAndClear() {
        List<Event> eventsCopy = new ArrayList<>(uncommittedEvents);
        uncommittedEvents.clear();
        return eventsCopy;
    }

    int rollDice() {
        int roll = rand.nextInt(5) + 1;
        raise(new PlayerRolledDiceEvent(this, roll));
        return roll;
    }
}
