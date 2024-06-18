package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import com.adaptionsoft.games.trivia.web.PlayerDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerFactory {
    private final EventPublisher eventPublisher;

    public Player create(UserId userId, String name) {
        return new Player(eventPublisher, userId, name);
    }

    public Player fromDto(PlayerDto dto) {
        return create(new UserId(dto.id()), dto.name()).withCoinCount(dto.coinCount());
    }

    public Player fromDto(UserDto dto) {
        return create(new UserId(dto.id()), dto.name());
    }
}
