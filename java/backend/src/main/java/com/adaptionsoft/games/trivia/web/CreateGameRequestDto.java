package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Player;
import com.adaptionsoft.games.trivia.domain.UserId;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import jakarta.validation.constraints.NotBlank;

public record CreateGameRequestDto(
        @NotBlank
        String gameName,
        @NotBlank
        UserDto creator
) {
    public Player getCreatorAsPlayerDomainObject(EventPublisher eventPublisher) {
        return new Player(eventPublisher, new UserId(creator.id()), creator.name());
    }
}
