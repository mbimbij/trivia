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

    private void addPlayer(String playerName) {
        players.add(new Player(playerName));
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
    }

    public void play() {
        do {
            rollDice();
            answerQuestion();
        } while (isGameInProgress);
    }

    private void answerQuestion() {
        if (this.rand.nextInt(9) == 7) {
            answerIncorrectly();
        } else {
            answerCorrectlyIfNotInJail();
        }
    }

    private void rollDice() {
        int roll = getDiceRoll();
        System.out.println(currentPlayer().getName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (currentPlayer().isInPenaltyBox()) {
            rollFromPenaltyBox(roll);
        } else {
            advanceCurrentPlayer(roll);
        }
    }

    private int getDiceRoll() {
        return this.rand.nextInt(5) + 1;
    }

    private void answerIncorrectly() {
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer().getName() + " was sent to the penalty box");
        currentPlayer().goToPenaltyBox();

        goToNextPlayer();
    }

    private void answerCorrectlyIfNotInJail() {
        if (!currentPlayer().isInPenaltyBox() || currentPlayer().isGettingOutOfPenaltyBox()) {
            answerCorrectly();
        } else {
            goToNextPlayer();
        }
    }

    private void answerCorrectly() {
        System.out.println("Answer was correct!!!!");
        currentPlayer().addCoin();
        System.out.printf("%s now has %d Gold Coins.%n", currentPlayer().getName(), currentPlayer().getCoinCount());
        endGameIfPlayerWon();
        goToNextPlayer();
    }

    private void endGameIfPlayerWon() {
        this.isGameInProgress = !hasPlayerWon();
    }

    private void goToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    private void rollFromPenaltyBox(int roll) {
        if (isPair(roll)) {
            currentPlayer().getOutOfPenaltyBox();
            System.out.println(currentPlayer().getName() + " is getting out of the penalty box");
            advanceCurrentPlayer(roll);
        } else {
            System.out.println(currentPlayer().getName() + " is not getting out of the penalty box");
            currentPlayer().stayInPenaltyBox();
        }
    }

    private static boolean isPair(int roll) {
        return roll % 2 != 0;
    }

    private void advanceCurrentPlayer(int roll) {
        changePlayersPosition(roll);
        printCurrentPlayersLocation();
        printCurrentCategory();
        currentPlayer().askQuestion();
    }

    private Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private void printCurrentCategory() {
        System.out.println("The category is " + currentPlayer().getQuestionCategory());
    }

    private void printCurrentPlayersLocation() {
        System.out.println(currentPlayer().getName()
                           + "'s new location is "
                           + currentPlayer().getLocation());
    }

    private void changePlayersPosition(int roll) {
        currentPlayer().advanceLocation(roll);
    }


    private boolean hasPlayerWon() {
        return (currentPlayer().getCoinCount() == 6);
    }
}
