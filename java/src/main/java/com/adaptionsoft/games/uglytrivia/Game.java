package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
    public Players players = new Players();

    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();

    public Game() {
        for (int i = 0; i < 50; i++) {
            popQuestions.addLast("Pop Question " + i);
            scienceQuestions.addLast(("Science Question " + i));
            sportsQuestions.addLast(("Sports Question " + i));
            rockQuestions.addLast(createRockQuestion(i));
        }
    }

    public String createRockQuestion(int index) {
        return "Rock Question " + index;
    }

    public void roll(int roll) {
        System.out.println(currentPlayer().getName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (currentPlayer().isInPenaltyBox()) {
            if (roll % 2 != 0) {
                currentPlayer().getOutOfPenaltyBox();
                System.out.println(currentPlayer().getName() + " is getting out of the penalty box");
                advanceCurrentPlayer(roll);
            } else {
                System.out.println(currentPlayer().getName() + " is not getting out of the penalty box");
                currentPlayer().stayInPenaltyBox();
            }

        } else {
            advanceCurrentPlayer(roll);
        }

    }

    private Player currentPlayer() {
        return players.getCurrentPlayer();
    }

    private void advanceCurrentPlayer(int roll) {
        changePlayersPosition(roll);
        printCurrentPlayersLocation();
        printCurrentCategory();
        askQuestion();
    }

    private void printCurrentCategory() {
        System.out.println("The category is " + currentCategory());
    }

    private void printCurrentPlayersLocation() {
        System.out.println(currentPlayer().getName()
                           + "'s new location is "
                           + currentPlayer().getLocation());
    }

    private void changePlayersPosition(int roll) {
        currentPlayer().advanceLocation(roll);
    }

    private void askQuestion() {
        if (currentCategory() == "Pop")
            System.out.println(popQuestions.removeFirst());
        if (currentCategory() == "Science")
            System.out.println(scienceQuestions.removeFirst());
        if (currentCategory() == "Sports")
            System.out.println(sportsQuestions.removeFirst());
        if (currentCategory() == "Rock")
            System.out.println(rockQuestions.removeFirst());
    }


    private String currentCategory() {
        if (currentPlayer().getLocation() % 4 == 0) return "Pop";
        if (currentPlayer().getLocation() % 4 == 1) return "Science";
        if (currentPlayer().getLocation() % 4 == 2) return "Sports";
        return "Rock";
    }

    public boolean wasCorrectlyAnswered() {
        if (currentPlayer().isInPenaltyBox()) {
            if (currentPlayer().isGettingOutOfPenaltyBox()) {
                System.out.println("Answer was correct!!!!");
                currentPlayer().addCoin();
                System.out.println(currentPlayer().getName()
                                   + " now has "
                                   + currentPlayer().getCoinCount()
                                   + " Gold Coins.");

                boolean hasPlayerNotWonYet = !hasPlayerWon();
                goToNextPlayer();

                return hasPlayerNotWonYet;
            } else {
                goToNextPlayer();
                return true;
            }


        } else {

            System.out.println("Answer was corrent!!!!");
            currentPlayer().addCoin();
            System.out.println(currentPlayer().getName()
                               + " now has "
                               + currentPlayer().getCoinCount()
                               + " Gold Coins.");

            boolean winner = !hasPlayerWon();
            goToNextPlayer();

            return winner;
        }
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer().getName() + " was sent to the penalty box");
        currentPlayer().goToPenaltyBox();

        goToNextPlayer();
        return true;
    }

    private void goToNextPlayer() {
        players.goToNextPlayer();
    }


    private boolean hasPlayerWon() {
        return (currentPlayer().getCoinCount() == 6);
    }

    public void add(String playerName) {
        players.add(playerName);
    }
}
