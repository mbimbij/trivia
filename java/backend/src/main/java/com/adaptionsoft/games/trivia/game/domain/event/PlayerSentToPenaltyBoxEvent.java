package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.Player;

public class PlayerSentToPenaltyBoxEvent extends PlayerEvent {
    public PlayerSentToPenaltyBoxEvent(Player player, int turn) {
        super(player, "%s was sent to the penalty box".formatted(player.getName()), turn);
    }
}
