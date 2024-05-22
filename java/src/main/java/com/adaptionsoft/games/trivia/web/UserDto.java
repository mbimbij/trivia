package com.adaptionsoft.games.trivia.web;

import jakarta.validation.constraints.NotBlank;

public record UserDto(
        @NotBlank
        String id,
        @NotBlank
        String name
) {
}
