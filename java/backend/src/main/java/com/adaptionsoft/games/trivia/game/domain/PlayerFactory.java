package com.adaptionsoft.games.trivia.game.domain;

import com.adaptionsoft.games.trivia.game.web.PlayerDto;
import com.adaptionsoft.games.trivia.game.web.UserDto;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventPublisher;
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
