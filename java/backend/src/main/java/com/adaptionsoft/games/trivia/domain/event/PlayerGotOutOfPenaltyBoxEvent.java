package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;

public class PlayerGotOutOfPenaltyBoxEvent extends PlayerEvent {
    public PlayerGotOutOfPenaltyBoxEvent(Player player) {
        super(player, "%s is getting out of the penalty box".formatted(player.getName()));
    }
}
