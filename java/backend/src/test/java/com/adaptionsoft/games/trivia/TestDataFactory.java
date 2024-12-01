package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.game.web.PlayerDto;
import org.mockito.Mockito;

public class TestDataFactory {
    public static PlayerDto aMockPlayerDto() {
        PlayerDto playerDto = Mockito.mock(PlayerDto.class);
        Mockito.when(playerDto.withTrimmedName()).thenReturn(Mockito.mock(PlayerDto.class));
        return playerDto;
    }
}
