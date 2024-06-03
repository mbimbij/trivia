package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;

public class PlayerSentToPenaltyBoxEvent extends PlayerEvent {
    public PlayerSentToPenaltyBoxEvent(Player player) {
        super(player, "%s was sent to the penalty box".formatted(player.getName()));
    }
}
