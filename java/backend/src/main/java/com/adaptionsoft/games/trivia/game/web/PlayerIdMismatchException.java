package com.adaptionsoft.games.trivia.game.web;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PlayerIdMismatchException extends RuntimeException {
    public PlayerIdMismatchException(String playerIdFromPath, String playerIdFromBody) {
        super("player id mismatch in request: player id from path: %s, player id from body: %s"
                .formatted(playerIdFromPath,playerIdFromBody));
    }
}
