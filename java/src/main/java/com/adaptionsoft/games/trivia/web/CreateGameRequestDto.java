package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Player;

public record CreateGameRequestDto(
        String gameName,
        UserDto creator
) {
    public Player toDomain() {
        return new Player(creator.id(), creator.name());
    }
}
