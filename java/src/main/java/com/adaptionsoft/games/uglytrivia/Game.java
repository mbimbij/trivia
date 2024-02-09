package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
    ArrayList<Player> players = new ArrayList<>();
    int[] places = new int[6];
    int[] purses = new int[6];
    boolean[] inPenaltyBox = new boolean[6];

    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();

    int currentPlayerIndex = 0;
    boolean isGettingOutOfPenaltyBox;

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

    public boolean add(String playerName) {
        players.add(new Player(playerName));

//        places[nbOfPlayers()] = 0;
        purses[nbOfPlayers()] = 0;
        inPenaltyBox[nbOfPlayers()] = false;

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    public int nbOfPlayers() {
        return players.size();
    }

    public void roll(int roll) {
        System.out.println(currentPlayer().getName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (inPenaltyBox[currentPlayerIndex]) {
            if (roll % 2 != 0) {
                getOutOfJail();
                System.out.println(currentPlayer().getName() + " is getting out of the penalty box");
                advanceCurrentPlayer(roll);
            } else {
                System.out.println(currentPlayer().getName() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {
            advanceCurrentPlayer(roll);
        }

    }

    private Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private void getOutOfJail() {
        isGettingOutOfPenaltyBox = true;
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
        if (inPenaltyBox[currentPlayerIndex]) {
            if (isGettingOutOfPenaltyBox) {
                System.out.println("Answer was correct!!!!");
                purses[currentPlayerIndex]++;
                System.out.println(currentPlayer().getName()
                                   + " now has "
                                   + purses[currentPlayerIndex]
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
            purses[currentPlayerIndex]++;
            System.out.println(currentPlayer().getName()
                               + " now has "
                               + purses[currentPlayerIndex]
                               + " Gold Coins.");

            boolean winner = !hasPlayerWon();
            goToNextPlayer();

            return winner;
        }
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer().getName() + " was sent to the penalty box");
        inPenaltyBox[currentPlayerIndex] = true;

        goToNextPlayer();
        return true;
    }

    private void goToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }


    private boolean hasPlayerWon() {
        return (purses[currentPlayerIndex] == 6);
    }
}
