package com.adaptionsoft.games.uglytrivia;

import java.util.Random;

public class Game {
    final Players players;

    private final Random rand;
    private final Board board;
    private boolean isGameInProgress = true;
    private Player currentPlayer;

    public Game(String... playersNames) {
        this(new Random(), playersNames);
    }

    // do not call directly, unless in a testing context
    public Game(Random rand, String... playersNames) {
        this.rand = rand;
        players = new Players(playersNames);
        currentPlayer = players.getCurrent();
        board = new Board();
    }

    public void play() {
        do {
            displayCurrentPlayer();
            int roll = rollDice();
            if (currentPlayer.isInPenaltyBox()) {
                playTurnFromPenaltyBox(roll);
            } else {
                playRegularTurn(roll);
            }
            endGameIfCurrentPlayerWon();
            goToNextPlayer();
        } while (isGameInProgress);
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

    private void goToNextPlayer() {
        players.goToNext();
        currentPlayer = players.getCurrent();
    }

    private void askQuestionToCurrentPlayer() {
        printCurrentCategory();
        board.drawQuestion(currentPlayer.getLocation());
        if (this.rand.nextInt(9) == 7) {
            System.out.println("Question was incorrectly answered");
            System.out.printf("%s was sent to the penalty box%n", currentPlayer.getName());
            currentPlayer.goToPenaltyBox();
        } else {
            System.out.println("Answer was correct!!!!");
            currentPlayer.addCoin();
            System.out.printf("%s now has %d Gold Coins.%n", currentPlayer.getName(), currentPlayer.getCoinCount());
        }
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
