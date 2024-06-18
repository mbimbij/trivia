package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.GameId;
import com.adaptionsoft.games.trivia.domain.Player;

public class PlayerWonEvent extends PlayerEvent {
    public PlayerWonEvent(GameId gameId, Player player, int turn) {
        super(player, "player %s won game %s".formatted(player.getId(), gameId), turn);
    }
}
