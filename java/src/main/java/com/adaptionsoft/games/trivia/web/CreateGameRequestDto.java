package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Player;
import jakarta.validation.constraints.NotBlank;

public record CreateGameRequestDto(
        @NotBlank
        String gameName,
        @NotBlank
        UserDto creator
) {
    public Player toDomain() {
        return new Player(creator.id(), creator.name());
    }
}
