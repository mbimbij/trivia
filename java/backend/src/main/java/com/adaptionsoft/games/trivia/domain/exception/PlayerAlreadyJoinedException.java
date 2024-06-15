package com.adaptionsoft.games.trivia.domain.exception;

import com.adaptionsoft.games.trivia.domain.Player;

public class PlayerAlreadyJoinedException extends BusinessException {
    public PlayerAlreadyJoinedException(Player newPlayer) {
        super("player with id %s already joined".formatted(newPlayer.getId()));
    }
}
