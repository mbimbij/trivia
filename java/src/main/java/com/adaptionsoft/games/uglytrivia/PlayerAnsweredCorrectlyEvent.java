package com.adaptionsoft.games.uglytrivia;

public class PlayerAnsweredCorrectlyEvent extends PlayerEvent {
    public PlayerAnsweredCorrectlyEvent(Player player, int turn) {
        super(player, turn, "Answer was correct!!!!");
    }
}
