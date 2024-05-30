package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.TestFixtures;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.adaptionsoft.games.trivia.domain.Game.State.CREATED;
import static com.adaptionsoft.games.trivia.domain.TestFixtures.GAME_TEST_ID;
import static org.assertj.core.api.Assertions.assertThat;

class GameResponseDtoShould {

    @Test
    void convertFromDomainObjectProperly() {
        // GIVEN
        Game actualGame = TestFixtures.a2playersGame();
        GameResponseDto expectedGameDto = buildExpectedGameDto(actualGame);

        // WHEN
        GameResponseDto actualGameDto = GameResponseDto.from(actualGame);

        // THEN
        assertThat(actualGameDto).usingRecursiveComparison().isEqualTo(expectedGameDto);
    }

    private GameResponseDto buildExpectedGameDto(Game game) {
        @jakarta.validation.constraints.NotBlank PlayerDto player1 = PlayerDto.from(game.getCreator());
        List<PlayerDto> players = game.getPlayersList().stream().map(PlayerDto::from).toList();
        return new GameResponseDto(GAME_TEST_ID, game.getName(), CREATED.toString(), 0, player1, players, player1);
    }
}