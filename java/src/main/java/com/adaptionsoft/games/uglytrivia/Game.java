package com.adaptionsoft.games.uglytrivia;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    final Players players;

    private final Random rand;
    private final Board board;
    private boolean isGameInProgress = true;
    @Getter
    private Player currentPlayer;
    @Getter
    private List<Event> uncommittedEvents = new ArrayList<>();
    int turn = 1;

    // do not call directly, unless in a testing context
    Game(Random rand, Board board, Players players) {
        this.rand = rand;
        this.players = players;
        currentPlayer = this.players.getCurrent();
        this.board = board;
        uncommittedEvents.addAll(players.getUncommittedEvents());
        uncommittedEvents.add(new GameCreatedEvent());
    }

    public void play() {
        do {
            performGameTurn();
        } while (isGameInProgress);
    }

    public void performGameTurn() {
        displayCurrentPlayer();
        int roll = rollDice();
        if (currentPlayer.isInPenaltyBox()) {
            playTurnFromPenaltyBox(roll);
        } else {
            playRegularTurn(roll);
        }
        endGameIfCurrentPlayerWon();
        endTurn();
    }

    private void displayCurrentPlayer() {
        System.out.println(currentPlayer.getName() + " is the current player");
    }

    private int rollDice() {
        int roll = this.rand.nextInt(5) + 1;
        System.out.println("They have rolled a " + roll);
        return roll;
    }

    private void playTurnFromPenaltyBox(int roll) {
        if (isPair(roll)) {
            System.out.println(currentPlayer.getName() + " is getting out of the penalty box");
            playRegularTurn(roll);
        } else {
            System.out.println(currentPlayer.getName() + " is not getting out of the penalty box");
        }
    }

    private boolean isPair(int roll) {
        return roll % 2 != 0;
    }

    private void playRegularTurn(int roll) {
        changePlayersPosition(roll);
        askQuestionToCurrentPlayer();
    }

    private void endGameIfCurrentPlayerWon() {
        this.isGameInProgress = !hasCurrentPlayerWon();
    }

    private void endTurn() {
        players.goToNextPlayerTurn();
        currentPlayer = players.getCurrent();
        turn++;
    }

    public void askQuestionToCurrentPlayer() {
        printCurrentCategory();
        board.drawQuestion(currentPlayer.getLocation());
        if (currentPlayer.isAnsweringCorrectly()) {
            answerCorrectly();
        } else {
            answerIncorrectly();
            askSecondQuestionAfterFirstIncorrectAnswer();
        }
    }

    private void askSecondQuestionAfterFirstIncorrectAnswer() {
        board.drawQuestion(currentPlayer.getLocation());
        if (currentPlayer.isAnsweringCorrectly()) {
            answerCorrectly();
        } else {
            answerIncorrectly();
            goToPenaltyBox();
        }
    }

    private void answerCorrectly() {
        System.out.println("Answer was correct!!!!");
        currentPlayer.addCoin();
        System.out.printf("%s now has %d Gold Coins.%n", currentPlayer.getName(), currentPlayer.getCoinCount());
        uncommittedEvents.add(new PlayerAnsweredCorrectlyEvent(currentPlayer, turn));
    }

    private void answerIncorrectly() {
        System.out.println("Question was incorrectly answered");
        uncommittedEvents.add(new PlayerAnsweredIncorrectlyEvent(currentPlayer, turn));
    }

    private void goToPenaltyBox() {
        System.out.printf("%s was sent to the penalty box%n", currentPlayer.getName());
        currentPlayer.goToPenaltyBox();
        uncommittedEvents.add(new PlayerSentToPenaltyBoxEvent(currentPlayer, turn));
    }

    private void printCurrentCategory() {
        System.out.println("The category is " + board.getQuestionCategory(currentPlayer.getLocation()));
    }

    private void printCurrentPlayerLocation() {
        System.out.println(currentPlayer.getName()
                           + "'s new location is "
                           + currentPlayer.getLocation());
    }

    private void changePlayersPosition(int roll) {
        currentPlayer.advanceLocation(roll);
        printCurrentPlayerLocation();
    }


    private boolean hasCurrentPlayerWon() {
        return (currentPlayer.getCoinCount() == 6);
    }

}
