package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class PlayerGotOutOfPenaltyBoxEvent extends PlayerEvent {
    public PlayerGotOutOfPenaltyBoxEvent(Player player) {
        super(player, "%s is getting out of the penalty box".formatted(player.getName()));
    }
}
