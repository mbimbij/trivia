package com.adaptionsoft.games.trivia.game.web;

import jakarta.validation.constraints.NotBlank;

public record CreateGameRequestDto(
        @NotBlank
        String gameName,
        @NotBlank
        UserDto creator
) {
}
