package com.adaptionsoft.games.trivia.game.domain;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

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
