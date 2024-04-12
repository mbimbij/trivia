package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;

public class PlayerWonEvent extends PlayerEvent {
    public PlayerWonEvent(Integer gameId, Player player) {
        super(player, "player id=%d won game id=%d".formatted(player.getId(), gameId));
    }
}
