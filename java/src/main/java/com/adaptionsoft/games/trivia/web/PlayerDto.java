package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Player;
import com.adaptionsoft.games.trivia.domain.UserId;
import jakarta.validation.constraints.NotBlank;

public record PlayerDto(
        @NotBlank
        String id,
        @NotBlank
        String name,
        @NotBlank
        int coinCount
) {

    public static PlayerDto from(Player player) {
        return new PlayerDto(player.getId().getValue(),
                player.getName(),
                player.getCoinCount());
    }

    public PlayerDto(@NotBlank String id, @NotBlank String name) {
        this(id, name, 0);
    }

    public Player toDomainObject() {
        return new Player(new UserId(id), name);
    }
}
