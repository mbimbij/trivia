package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.web.UserDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class Actor {
    @NotBlank
    protected final String id;
    @NotBlank
    protected final String name;
    @Getter
    @Setter
    protected boolean isLoggedIn = false;

    public UserDto toUserDto(){
        return new UserDto(id, name);
    }
}
