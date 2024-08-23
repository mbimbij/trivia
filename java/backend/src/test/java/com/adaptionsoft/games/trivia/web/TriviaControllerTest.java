package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameFactory;
import com.adaptionsoft.games.trivia.domain.GameRepository;
import com.adaptionsoft.games.trivia.domain.exception.GameNotFoundException;
import com.adaptionsoft.games.trivia.domain.exception.PlayerNotFoundInGameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class TriviaControllerTest {
    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameFactory gameFactory;
    @InjectMocks
    private GameController controller;

    @BeforeEach
    void setUp() {
        Mockito.reset(gameRepository, gameFactory);
    }

    @Test
    void given_game_not_found__should_thrown_exception() {
        Mockito.doReturn(Optional.empty()).when(gameRepository).findById(any());
        assertSoftly(softAssertions -> {
            softAssertions.assertThatThrownBy(() -> controller.joinGame(-1, null, Mockito.mock(PlayerDto.class)))
                    .isInstanceOf(GameNotFoundException.class);
            softAssertions.assertThatThrownBy(() -> controller.startGame(-1, "notExistingPlayer"))
                    .isInstanceOf(GameNotFoundException.class);
        });
    }

    @Test
    void given_player_not_found_in_game__should_thrown_exception() {
        Game game = Mockito.mock(Game.class);
        Mockito.doReturn(Optional.empty()).when(game).findPlayerById(any());
        Mockito.doReturn(Optional.of(game)).when(gameRepository).findById(any());
        assertSoftly(softAssertions -> {
            softAssertions.assertThatThrownBy(() -> controller.startGame(-1, "notExistingPlayer"))
                    .isInstanceOf(PlayerNotFoundInGameException.class);
        });
    }
}