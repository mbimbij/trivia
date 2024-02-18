package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class PlayerSentToPenaltyBoxEvent extends PlayerEvent {
    public PlayerSentToPenaltyBoxEvent(Player player) {
        super(player, "%s was sent to the penalty box".formatted(player.getName()));
    }
}
