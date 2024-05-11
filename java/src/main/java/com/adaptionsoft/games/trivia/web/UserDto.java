package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Player;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
        @NotBlank
        Integer id,
        @NotBlank
        String name
) {
    public static UserDto from(Player player) {
        return new UserDto(player.getId(), player.getName());
    }
}
