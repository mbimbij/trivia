package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class PlayerStayedInPenaltyBoxEvent extends PlayerEvent {
    public PlayerStayedInPenaltyBoxEvent(Player player) {
        super(player, "%s is not getting out of the penalty box".formatted(player.getName()));
    }
}
