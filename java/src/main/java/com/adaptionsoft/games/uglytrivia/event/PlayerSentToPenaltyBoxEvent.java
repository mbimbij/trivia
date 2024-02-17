package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class PlayerSentToPenaltyBoxEvent extends PlayerEvent {
    public PlayerSentToPenaltyBoxEvent(Player player, int turn) {
        super(player, turn, "%s was sent to the penalty box".formatted(player.getName()));
    }
}
