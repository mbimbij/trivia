package com.adaptionsoft.games.trivia.game.domain;

import jakarta.validation.constraints.NotNull;

public record AvailableAswers(
        @NotNull
        String A,
        @NotNull
        String B,
        @NotNull
        String C,
        @NotNull
        String D
) {
}
