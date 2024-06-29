package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.web.GameResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TestActor {
    @NotBlank
    protected final String id;
    @NotBlank
    protected final String name;

    public abstract void join(GameResponseDto game);
    public abstract void start(GameResponseDto game);
    public abstract void rollDice();
}
