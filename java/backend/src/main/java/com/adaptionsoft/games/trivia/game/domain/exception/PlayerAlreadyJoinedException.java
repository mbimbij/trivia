package com.adaptionsoft.games.trivia.game.domain.exception;

import com.adaptionsoft.games.trivia.game.domain.Player;

public class PlayerAlreadyJoinedException extends PlayerException {
    public PlayerAlreadyJoinedException(Player newPlayer) {
        super(newPlayer.getGameId(), newPlayer.getId(), "player with id %s already joined".formatted(newPlayer.getId()));
    }
}
