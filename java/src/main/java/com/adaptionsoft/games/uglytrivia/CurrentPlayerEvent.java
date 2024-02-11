package com.adaptionsoft.games.uglytrivia;

public class CurrentPlayerEvent extends PlayerEvent {
    public CurrentPlayerEvent(Player player, int turn) {
        super(player, turn, "%s is the current player");
    }
}
