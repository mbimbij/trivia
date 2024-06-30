package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.web.GameResponseDto;
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

    public abstract void createGame(String gameName);
    public abstract void join(int gameId);
    public abstract void start(GameResponseDto game);
    public abstract void rollDice();
    public UserDto toUserDto(){
        return new UserDto(id, name);
    }
}
