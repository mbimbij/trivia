package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.Player;

public class PlayerGotOutOfPenaltyBoxEvent extends PlayerEvent {
    public PlayerGotOutOfPenaltyBoxEvent(Player player, int turn) {
        super(player, "%s is getting out of the penalty box".formatted(player.getName()), turn);
    }
}
