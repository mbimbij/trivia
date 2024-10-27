package com.adaptionsoft.games.trivia.game.web;

import jakarta.validation.constraints.NotBlank;

public record UserDto(
        @NotBlank
        String id,
        @NotBlank
        String name
) {
    public UserDto withTrimmedName() {
        return new UserDto(id, name.trim());
    }
}
