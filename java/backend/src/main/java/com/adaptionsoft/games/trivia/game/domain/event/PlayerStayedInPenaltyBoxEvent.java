package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.Player;

public class PlayerStayedInPenaltyBoxEvent extends PlayerEvent {
    public PlayerStayedInPenaltyBoxEvent(Player player, int turn) {
        super(player, "%s is not getting out of the penalty box".formatted(player.getName()), turn);
    }
}
