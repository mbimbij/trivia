package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class PlayerGotOutOfPenaltyBoxEvent extends PlayerEvent {
    public PlayerGotOutOfPenaltyBoxEvent(Player player, int turn) {
        super(player, turn, "%s is getting out of the penalty box".formatted(player.getName()));
    }
}
