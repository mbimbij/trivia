package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.web.GameResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TestPlayer {
    @NotBlank
    protected final String id;
    @NotBlank
    protected final String name;

    abstract void join(GameResponseDto game);
    abstract void start(GameResponseDto game);
}
