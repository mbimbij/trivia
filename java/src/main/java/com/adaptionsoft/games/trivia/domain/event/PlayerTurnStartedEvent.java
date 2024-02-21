package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;

public class PlayerTurnStartedEvent extends PlayerEvent {
    public PlayerTurnStartedEvent(Player player) {
        super(player, "%s is the current player".formatted(player.getName()));
    }
}
