package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private final EventPublisher eventPublisher;
    private final List<Event> uncommittedEvents = new ArrayList<>();
    private int turn = 1;
    @Setter // for testing purposes only
    private int consecutiveCorrectAnswersCount;
    private final Board board;

    public Player(String name, AnsweringStrategy answeringStrategy, EventPublisher eventPublisher, Board board) {
        this.name = name;
        this.answeringStrategy = answeringStrategy;
        this.eventPublisher = eventPublisher;
        this.board = board;
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

    void answerCorrectly() {
        if (isOnACorrectAnswersStreak()) {
            addCoin();
        }

        addCoin();
        publish(new PlayerAnsweredCorrectlyEvent(this),
                new CoinAddedToPlayerEvent(this)
        );
        consecutiveCorrectAnswersCount++;
    }

    void answerIncorrectly() {
        publish(new PlayerAnsweredIncorrectlyEvent(this));
        if(!isOnACorrectAnswersStreak()){
            goToPenaltyBox();
            publish(new PlayerSentToPenaltyBoxEvent(this));
        }
        consecutiveCorrectAnswersCount = 0;
    }

    private void publish(Event... events) {
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
        publish(new QuestionAskedToPlayerEvent(this, question));
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

    public List<Event> getUncommittedEventsAndClear() {
        List<Event> eventsCopy = new ArrayList<>(uncommittedEvents);
        uncommittedEvents.clear();
        return eventsCopy;
    }
}
