package com.adaptionsoft.games.uglytrivia;

public class PlayerAnsweredIncorrectlyEvent extends PlayerEvent {
    public PlayerAnsweredIncorrectlyEvent(Player player, int turn) {
        super(player, turn, "Question was incorrectly answered");
    }
}
