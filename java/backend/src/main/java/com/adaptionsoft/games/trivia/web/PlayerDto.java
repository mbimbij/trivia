package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Player;
import com.adaptionsoft.games.trivia.domain.statemachine.State;
import jakarta.validation.constraints.NotBlank;
import lombok.With;

@With
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
        int consecutiveIncorrectAnswersCount,
        @NotBlank
        State state,
        @NotBlank
        int location,
        @NotBlank
        boolean gotOutOfPenaltyBox
) {
    public static PlayerDto from(Player player) {
        if (player == null) return null;
        return new PlayerDto(player.getId().getValue(),
                player.getName(),
                player.getCoinCount(),
                player.isInPenaltyBox(),
                player.getConsecutiveIncorrectAnswersCount(),
                player.getCurrentState(),
                player.getLocation(),
                player.isGotOutOfPenaltyBox());
    }

}
