package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.domain.exception.CannotUpdateLocationFromPenaltyBoxException;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.*;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PUBLIC;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
public class Player extends Entity<UserId> {
    @Setter
    private String name;

    @With // for testing purposes only
    private int coinCount;

    @Setter
    private int location;
    private int turn = 1;
    @Setter(PACKAGE)
    private boolean isInPenaltyBox;
    @Setter // for testing purposes only
    private int consecutiveCorrectAnswersCount;
    @Setter // for testing purposes only
    private int consecutiveIncorrectAnswersCount;
    @Setter
    private GameId gameId;

    public Player(EventPublisher eventPublisher, UserId playerId, String name) {
        super(playerId, eventPublisher);
        this.name = name;
    }

    void incrementTurn() {
        turn++;
    }

    boolean isWinning() {
        return (coinCount >= 12);
    }

    boolean canContinueAfterIncorrectAnswer() {
        int maxAllowed = 2;
        return consecutiveIncorrectAnswersCount < maxAllowed;
    }

    /**
     * Used externally by tests ONLY
     */
    // TODO déplacer vers Game ?
    void answerCorrectly() {
        if (isOnAStreak()) {
            addCoin();
        }

        addCoin();
        consecutiveCorrectAnswersCount++;
        consecutiveIncorrectAnswersCount = 0;
        raise(new PlayerAnsweredCorrectlyEvent(this, this.getTurn()),
                new CoinAddedToPlayerEvent(this, this.getTurn())
        );
    }

    /**
     * Used externally by tests ONLY
     */
    boolean isOnAStreak() {
        return consecutiveCorrectAnswersCount >= 3;
    }

    private void addCoin() {
        coinCount++;
    }

    /**
     * Used externally by tests ONLY
     */
    // TODO déplacer vers Game ?
    void answerIncorrectly() {
        raise(new PlayerAnsweredIncorrectlyEvent(this, this.getTurn()));
        consecutiveIncorrectAnswersCount++;
        if (consecutiveIncorrectAnswersCount >= 2) {
            goToPenaltyBox();
        }
        consecutiveCorrectAnswersCount = 0;
    }

    private void goToPenaltyBox() {
        isInPenaltyBox = true;
        raise(new PlayerSentToPenaltyBoxEvent(this, this.getTurn()));
    }

    void updateLocation(int newLocation) {
        if(isInPenaltyBox){
            throw new CannotUpdateLocationFromPenaltyBoxException(gameId, id);
        }
        setLocation(newLocation);
        Questions.Category category = Questions.Category.getQuestionCategory(getLocation());
        raise(new PlayerChangedLocationEvent(this,category, this.getTurn()));
    }

    public boolean canRollDice() {
        return isInPenaltyBox || consecutiveIncorrectAnswersCount == 0;
    }

    public void getOutOfPenaltyBox() {
        isInPenaltyBox = false;
        consecutiveCorrectAnswersCount = 0;
        consecutiveIncorrectAnswersCount = 0;
    }
}
