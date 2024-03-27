package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Player;

public record UserDto(
        Integer id,
        String name
) {
    public static UserDto from(Player player) {
        return new UserDto(player.getId(), player.getName());
    }
}
