package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.domain.exception.CannotUpdateLocationFromPenaltyBoxException;
import com.adaptionsoft.games.trivia.domain.statemachine.State;
import com.adaptionsoft.games.trivia.domain.statemachine.StateManager;
import com.adaptionsoft.games.trivia.domain.statemachine.Transition;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.*;

import static com.adaptionsoft.games.trivia.domain.PlayerAction.*;
import static com.adaptionsoft.games.trivia.domain.PlayerState.*;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
public class Player extends Entity<UserId> {
    @Setter
    private String name;

    @With // for testing purposes only
    @Setter // for testing purposes only
    private int coinCount;

    @Setter
    private int location;
    private int turn = 1;
    private boolean isInPenaltyBox;
    @Getter
    @Setter
    private boolean gotOutOfPenaltyBox;
    @Setter // for testing purposes only
    private int consecutiveCorrectAnswersCount;
    @Setter // for testing purposes only
    private int consecutiveIncorrectAnswersCount;
    @Setter
    private GameId gameId;
    @Setter
    private StateManager stateManager;

    public Player(EventPublisher eventPublisher, UserId playerId, String name) {
        super(playerId, eventPublisher);
        this.name = name;
        this.stateManager = new StateManager("player - name:%s - id:%s".formatted(name, playerId), WAITING_FOR_DICE_ROLL,
                new Transition(WAITING_FOR_DICE_ROLL, ROLL_DICE, WAITING_TO_UPDATE_LOCATION),
                new Transition(WAITING_TO_UPDATE_LOCATION, UPDATE_LOCATION, WAITING_TO_DRAW_1ST_QUESTION),
                new Transition(WAITING_TO_DRAW_1ST_QUESTION, DRAW_QUESTION, WAITING_FOR_1ST_ANSWER),
                new Transition(WAITING_FOR_1ST_ANSWER, SUBMIT_ANSWER, WAITING_FOR_1ST_ANSWER_EVALUATION),

                new Transition(WAITING_FOR_1ST_ANSWER_EVALUATION, ANSWER_CORRECTLY, WAITING_TO_VALIDATE_FIRST_CORRECT_ANSWER),
                new Transition(WAITING_FOR_1ST_ANSWER_EVALUATION, ANSWER_INCORRECTLY, WAITING_TO_VALIDATE_FIRST_INCORRECT_ANSWER),

                new Transition(WAITING_TO_VALIDATE_FIRST_CORRECT_ANSWER, VALIDATE, WAITING_TO_END_TURN_OR_GAME),
                new Transition(WAITING_TO_VALIDATE_FIRST_INCORRECT_ANSWER, VALIDATE, WAITING_TO_DRAW_2ND_QUESTION),

                new Transition(WAITING_TO_DRAW_2ND_QUESTION, DRAW_QUESTION, WAITING_FOR_2ND_ANSWER),
                new Transition(WAITING_FOR_2ND_ANSWER, SUBMIT_ANSWER, WAITING_FOR_2ND_ANSWER_EVALUATION),

                new Transition(WAITING_FOR_2ND_ANSWER_EVALUATION, ANSWER_CORRECTLY, WAITING_TO_VALIDATE_SECOND_CORRECT_ANSWER),
                new Transition(WAITING_FOR_2ND_ANSWER_EVALUATION, ANSWER_INCORRECTLY, WAITING_TO_VALIDATE_SECOND_INCORRECT_ANSWER),

                new Transition(WAITING_TO_VALIDATE_SECOND_CORRECT_ANSWER, VALIDATE, WAITING_TO_END_TURN_OR_GAME),
                new Transition(WAITING_TO_VALIDATE_SECOND_INCORRECT_ANSWER, VALIDATE, IN_PENALTY_BOX),

                new Transition(WAITING_TO_END_TURN_OR_GAME, END_TURN, WAITING_FOR_DICE_ROLL),
                new Transition(WAITING_TO_END_TURN_OR_GAME, END_GAME, GAME_END),
                new Transition(IN_PENALTY_BOX, ROLL_DICE, WAITING_FOR_ROLL_DICE_EVALUATION),
                new Transition(IN_PENALTY_BOX, END_TURN, IN_PENALTY_BOX),

                new Transition(WAITING_FOR_ROLL_DICE_EVALUATION, ROLL_EVEN_NUMBER_IN_PENALTY_BOX, WAITING_TO_VALIDATE_EVEN_DICE_ROLL_FROM_PENALTY_BOX),
                new Transition(WAITING_FOR_ROLL_DICE_EVALUATION, ROLL_ODD_NUMBER_IN_PENALTY_BOX, WAITING_TO_VALIDATE_ODD_DICE_ROLL_FROM_PENALTY_BOX),

                new Transition(WAITING_TO_VALIDATE_EVEN_DICE_ROLL_FROM_PENALTY_BOX, VALIDATE, WAITING_TO_UPDATE_LOCATION),
                new Transition(WAITING_TO_VALIDATE_ODD_DICE_ROLL_FROM_PENALTY_BOX, VALIDATE, IN_PENALTY_BOX)
        );
    }

    public State getCurrentState() {
        return stateManager.getCurrentState();
    }

    void incrementTurn() {
        turn++;
    }

    /**
     * For testing only
     * @param inPenaltyBox
     */
    public void setInPenaltyBox(boolean inPenaltyBox) {
        isInPenaltyBox = inPenaltyBox;
        if(inPenaltyBox) {
            stateManager.setCurrentState(IN_PENALTY_BOX);
        }
    }

    boolean isWinning() {
        return (coinCount >= 6);
    }

    /**
     * Used externally by tests ONLY
     */
    // TODO déplacer vers Game ?
    void answerCorrectly() {
        stateManager.validateAction(ANSWER_CORRECTLY);
        if (isOnAStreak()) {
            addCoin();
        }

        addCoin();
        consecutiveCorrectAnswersCount++;
        consecutiveIncorrectAnswersCount = 0;
        stateManager.applyAction(ANSWER_CORRECTLY);
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
        stateManager.validateAction(ANSWER_INCORRECTLY);
        raise(new PlayerAnsweredIncorrectlyEvent(this, this.getTurn()));
        stateManager.applyAction(ANSWER_INCORRECTLY);
        consecutiveIncorrectAnswersCount++;
        if (consecutiveIncorrectAnswersCount >= 2) {
            goToPenaltyBox();
        }
        consecutiveCorrectAnswersCount = 0;
    }

    public void goToPenaltyBox() {
        isInPenaltyBox = true;
        raise(new PlayerSentToPenaltyBoxEvent(this, this.getTurn()));
    }

    void updateLocation(int newLocation) {
        if (isInPenaltyBox) {
            throw new CannotUpdateLocationFromPenaltyBoxException(gameId, id);
        }
        setLocation(newLocation);
        QuestionsDeck.Category category = QuestionsDeck.Category.getQuestionCategory(getLocation());
        raise(new PlayerChangedLocationEvent(this, category, this.getTurn()));
    }

    public boolean canRollDice() {
        return isInPenaltyBox || consecutiveIncorrectAnswersCount == 0;
    }

    public void getOutOfPenaltyBox() {
        isInPenaltyBox = false;
        gotOutOfPenaltyBox = true;
        consecutiveCorrectAnswersCount = 0;
        consecutiveIncorrectAnswersCount = 0;
        raise(new PlayerGotOutOfPenaltyBoxEvent(this, getTurn()));
    }

    // TODO apply refactoring Remove Middle Man
    public void validateAction(PlayerAction playerAction) {
        stateManager.validateAction(playerAction);
    }

    // TODO apply refactoring Remove Middle Man
    public void applyAction(PlayerAction playerAction) {
        stateManager.applyAction(playerAction);
    }

    public void setState(PlayerState playerState) {
        stateManager.setCurrentState(playerState);
    }

    State getState() {
        return getStateManager().getCurrentState();
    }

     private void rollDiceFromPenaltyBox(Dice.Roll currentRoll) {
        if (currentRoll.isPair()) {
            stateManager.applyAction(ROLL_EVEN_NUMBER_IN_PENALTY_BOX);
            getOutOfPenaltyBox();
        } else {
            this.applyAction(ROLL_ODD_NUMBER_IN_PENALTY_BOX);
            raise(new PlayerStayedInPenaltyBoxEvent(this, this.getTurn()));
        }
    }

    void applyDiceRoll(Dice.Roll currentRoll, int newLocation) {
        this.validateAction(ROLL_DICE);
        this.applyAction(ROLL_DICE);
        raise(new PlayerRolledDiceEvent(this, currentRoll, this.getTurn()));
        if (this.isInPenaltyBox()) {
            rollDiceFromPenaltyBox(currentRoll);
        } else {
            this.updateLocation(newLocation);
            this.applyAction(UPDATE_LOCATION);
        }
    }
}
