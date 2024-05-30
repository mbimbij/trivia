package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Player;
import com.adaptionsoft.games.trivia.domain.UserId;
import jakarta.validation.constraints.NotBlank;

public record CreateGameRequestDto(
        @NotBlank
        String gameName,
        @NotBlank
        UserDto creator
) {
    public Player getCreatorAsDomainObject() {
        return new Player(new UserId(creator.id()), creator.name());
    }
}
