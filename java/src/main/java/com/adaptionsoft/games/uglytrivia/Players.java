package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;

public class Players {
    ArrayList<Player> playersList = new ArrayList<>();
    int currentPlayerIndex = 0;

    public boolean add(String playerName) {
        playersList.add(new Player(playerName));
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + playersList.size());
        return true;
    }

    public Player getCurrentPlayer() {
        return playersList.get(currentPlayerIndex);
    }

    public void goToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playersList.size();
    }
}
