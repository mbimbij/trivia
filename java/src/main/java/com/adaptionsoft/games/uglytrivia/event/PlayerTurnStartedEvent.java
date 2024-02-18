package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class PlayerTurnStartedEvent extends PlayerEvent {
    public PlayerTurnStartedEvent(Player player) {
        super(player, "%s is the current player".formatted(player.getName()));
    }
}
