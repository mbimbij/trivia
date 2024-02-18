package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.*;
import lombok.*;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Player {
    @EqualsAndHashCode.Include
    private final String name;
    private final AnsweringStrategy answeringStrategy;
    private boolean isInPenaltyBox;
    @With // for testing purposes only
    private int coinCount;
    private int location;
    private final EventPublisher eventPublisher;
    private int turn = 1;
    @Setter // for testing purposes only
    private int consecutiveCorrectAnswersCount;
    private int consecutiveIncorrectAnswersCount;
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
        consecutiveIncorrectAnswersCount = 0;
        if (isOnAWinningStreak()) {
            addCoin();
        }

        addCoin();
        publish(new PlayerAnsweredCorrectlyEvent(this, turn),
                new CoinAddedToPlayerEvent(this, turn)
        );
        increaseStreakForSubsequentCorrectAnswers();
    }

    private void increaseStreakForSubsequentCorrectAnswers() {
        consecutiveCorrectAnswersCount++;
    }

    void answerIncorrectly() {
        consecutiveCorrectAnswersCount = 0;
        consecutiveIncorrectAnswersCount++;
        publish(new PlayerAnsweredIncorrectlyEvent(this, turn));
        if (consecutiveIncorrectAnswersCount >= 2) {
            goToPenaltyBox();
            publish(new PlayerSentToPenaltyBoxEvent(this, turn));
        }
    }

    private void publish(Event... events) {
        eventPublisher.publish(events);
    }

    public void incrementTurn() {
        turn++;
    }


    public boolean isOnAWinningStreak() {
        return consecutiveCorrectAnswersCount >= 3;
    }

    void drawQuestion() {
        String question = board.drawQuestion(getLocation());
        publish(new QuestionAskedToPlayerEvent(this, turn, question));
    }

    public void askQuestion() {
        drawQuestion();
        if (answeringStrategy.isAnsweringCorrectly()) {
            answerCorrectly();
        } else {
            answerIncorrectly();
            drawQuestion();
            if (answeringStrategy.isAnsweringCorrectly()) {
                answerCorrectly();
            } else {
                answerIncorrectly();
            }
        }
    }

    boolean isWinning() {
        return (getCoinCount() >= 12);
    }
}
