package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class PlayerTurnStartedEvent extends PlayerEvent {
    public PlayerTurnStartedEvent(Player player, int turn) {
        super(player, turn, "%s is the current player".formatted(player.getName()));
    }
}
