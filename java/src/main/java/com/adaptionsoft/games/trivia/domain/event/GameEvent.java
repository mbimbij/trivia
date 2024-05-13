package com.adaptionsoft.games.trivia.domain.event;

import lombok.Getter;

@Getter
public class GameEvent extends Event {
    // TODO ajouter à quel tour de jeu un event a lieu
    protected GameEvent(Integer gameId, String stringValue) {
        super(gameId, stringValue);
    }
}
