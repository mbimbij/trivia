package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.Player;

public class PlayerTurnStartedEvent extends PlayerEvent {
    public PlayerTurnStartedEvent(Player player, int turn) {
        super(player, "%s is the current player".formatted(player.getName()), turn);
    }
}
