package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;
import com.adaptionsoft.games.uglytrivia.event.PlayerEvent;

public class PlayerStayedInPenaltyBoxEvent extends PlayerEvent {
    public PlayerStayedInPenaltyBoxEvent(Player player, int turn) {
        super(player, turn, "%s is not getting out of the penalty box".formatted(player.getName()));
    }
}
