package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.TestFixtures;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.adaptionsoft.games.trivia.domain.Game.State.CREATED;
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
        UserDto player1 = UserDto.from(game.getCreator());
        List<UserDto> players = game.getPlayersList().stream().map(UserDto::from).toList();
        return new GameResponseDto(1, game.getName(), CREATED.toString(), 1, player1, players, player1);
    }
}