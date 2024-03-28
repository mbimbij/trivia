package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;

public class PlayerStayedInPenaltyBoxEvent extends PlayerEvent {
    public PlayerStayedInPenaltyBoxEvent(Player player) {
        super(player, "%s is not getting out of the penalty box".formatted(player.getName()));
    }
}
