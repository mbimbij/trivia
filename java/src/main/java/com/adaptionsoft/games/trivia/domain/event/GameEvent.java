package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.GameId;
import lombok.Getter;

@Getter
public class GameEvent extends Event {
    // TODO ajouter à quel tour de jeu un event a lieu
    protected GameEvent(GameId gameId, String stringValue) {
        super(gameId, stringValue);
    }
}
