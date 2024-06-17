package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.microarchitecture.EventRaiser;

import java.util.Random;

public class PlayerTurnOrchestrator extends EventRaiser {
    private final Questions questions;
    private final Random rand;
    private final Board board;
    private Player currentPlayer;

    public PlayerTurnOrchestrator(Questions questions, Random rand, Board board) {
        this.questions = questions;
        this.rand = rand;
        this.board = board;
    }

    public void performTurn(Player player) {
        currentPlayer = player;
        int roll = rollDice();
        if (currentPlayer.isInPenaltyBox()) {
            playFromPenaltyBox(roll);
        } else {
            playRegularTurn(roll);
        }
    }

    int rollDice() {
        int roll = rand.nextInt(5) + 1;
        raise(new PlayerRolledDiceEvent(currentPlayer, roll));
        return roll;
    }

    void playFromPenaltyBox(int roll) {
        if (isPair(roll)) {
            raise(new PlayerGotOutOfPenaltyBoxEvent(currentPlayer));
            playRegularTurn(roll);
        } else {
            raise(new PlayerStayedInPenaltyBoxEvent(currentPlayer));
        }
    }

    void playRegularTurn(int roll) {
        currentPlayer.updateLocation(computeNewPlayerLocation(roll));
        askQuestion();
    }

    void askQuestion() {
        boolean isAnswerCorrect;
        do {
            isAnswerCorrect = doAskQuestion();
        } while (!isAnswerCorrect && currentPlayer.canContinueAfterIncorrectAnswer());
    }

    boolean doAskQuestion() {
        Question question = questions.drawQuestion(currentPlayer.getLocation());
        raise(new QuestionAskedToPlayerEvent(currentPlayer, question.questionText()));
        if (isAnsweringCorrectly()) {
            currentPlayer.answerCorrectly();
            return true;
        } else {
            currentPlayer.answerIncorrectly();
            return false;
        }
    }

    int computeNewPlayerLocation(int roll) {
        return (currentPlayer.getLocation() + roll) % board.getSquaresCount();
    }

    boolean isPair(int roll) {
        return roll % 2 != 0;
    }

    boolean isAnsweringCorrectly() {
        return rand.nextInt(9) != 7;
    }
}