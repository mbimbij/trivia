package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.GameId;
import com.adaptionsoft.games.trivia.shared.microarchitecture.Event;
import lombok.Getter;

@Getter
public class GameEvent extends Event {
    // TODO ajouter à quel tour de jeu un event a lieu
    protected GameEvent(GameId gameId, String stringValue) {
        super(gameId, stringValue);
    }
}
