package com.adaptionsoft.games.trivia.web;

import jakarta.validation.constraints.NotBlank;

public record CreateGameRequestDto(
        @NotBlank
        String gameName,
        @NotBlank
        UserDto creator
) {
}
