package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerFactory {
    private final EventPublisher eventPublisher;

    public Player create(UserId userId, String name) {
        return new Player(eventPublisher, userId, name);
    }
}
