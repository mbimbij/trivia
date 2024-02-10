package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game {
    List<Player> players = new ArrayList<>();

    int currentPlayerIndex = 0;
    private final Random rand;
    private boolean isGameInProgress = true;

    public Game(String... playersNames) {
        this(new Random(), playersNames);
    }

    // do not call directly, unless in a testing context
    public Game(Random rand, String... playersNames) {
        this.rand = rand;
        Arrays.stream(playersNames).forEach(this::addPlayer);
        for (int i = 0; i < 50; i++) {
            Questions.POP.stackCard("Pop Question " + i);
            Questions.SCIENCE.stackCard("Science Question " + i);
            Questions.SPORTS.stackCard("Sports Question " + i);
            Questions.ROCK.stackCard("Rock Question " + i);
        }
    }

    public void play() {
        do {
            displayCurrentPlayer();
            int roll = rollDice();
            if (currentPlayer().isInPenaltyBox()) {
                playTurnFromPenaltyBox(roll);
            } else {
                playRegularTurn(roll);
            }
            endGameIfCurrentPlayerWon();
            goToNextPlayer();
        } while (isGameInProgress);
    }

    private void addPlayer(String playerName) {
        players.add(new Player(playerName));
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
    }

    private void displayCurrentPlayer() {
        System.out.println(currentPlayer().getName() + " is the current player");
    }

    private int rollDice() {
        int roll = this.rand.nextInt(5) + 1;
        System.out.println("They have rolled a " + roll);
        return roll;
    }

    private void playTurnFromPenaltyBox(int roll) {
        if (isPair(roll)) {
            currentPlayer().getOutOfPenaltyBox();
            System.out.println(currentPlayer().getName() + " is getting out of the penalty box");
            playRegularTurn(roll);
        } else {
            System.out.println(currentPlayer().getName() + " is not getting out of the penalty box");
            currentPlayer().stayInPenaltyBox();
        }
    }

    private void playRegularTurn(int roll) {
        changePlayersPosition(roll);
        askQuestionToCurrentPlayer();
    }

    private void askQuestionToCurrentPlayer() {
        printCurrentCategory();
        currentPlayer().askQuestion();
        if (this.rand.nextInt(9) == 7) {
            System.out.println("Question was incorrectly answered");
            System.out.printf("%s was sent to the penalty box%n", currentPlayer().getName());
            currentPlayer().goToPenaltyBox();
        } else {
            System.out.println("Answer was correct!!!!");
            currentPlayer().addCoin();
            System.out.printf("%s now has %d Gold Coins.%n", currentPlayer().getName(), currentPlayer().getCoinCount());
        }
    }

    private void endGameIfCurrentPlayerWon() {
        this.isGameInProgress = !hasCurrentPlayerWon();
    }

    private void goToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    private boolean isPair(int roll) {
        return roll % 2 != 0;
    }

    private Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private void printCurrentCategory() {
        System.out.println("The category is " + currentPlayer().getQuestionCategory());
    }

    private void printCurrentPlayerLocation() {
        System.out.println(currentPlayer().getName()
                           + "'s new location is "
                           + currentPlayer().getLocation());
    }

    private void changePlayersPosition(int roll) {
        currentPlayer().advanceLocation(roll);
        printCurrentPlayerLocation();
    }


    private boolean hasCurrentPlayerWon() {
        return (currentPlayer().getCoinCount() == 6);
    }
}
