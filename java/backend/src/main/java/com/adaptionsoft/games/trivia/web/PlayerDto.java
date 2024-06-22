package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Player;
import jakarta.validation.constraints.NotBlank;

public record PlayerDto(
        @NotBlank
        String id,
        @NotBlank
        String name,
        @NotBlank
        int coinCount,
        @NotBlank
        boolean isInPenaltyBox,
        @NotBlank
        int consecutiveIncorrectAnswersCount
) {
    public static PlayerDto from(Player player) {
        if (player == null) return null;
        return new PlayerDto(player.getId().getValue(),
                player.getName(),
                player.getCoinCount(),
                player.isInPenaltyBox(),
                player.getConsecutiveIncorrectAnswersCount());
    }
}
