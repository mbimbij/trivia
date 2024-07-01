package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.web.UserDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class TestActor {
    @NotBlank
    protected final String id;
    @NotBlank
    protected final String name;

    public abstract void join(int gameId);
    public abstract void start(int game);
    public abstract void rollDice();
    public UserDto toUserDto(){
        return new UserDto(id, name);
    }
}
