package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.TestFixtures;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.adaptionsoft.games.trivia.domain.Game.State.CREATED;
import static org.assertj.core.api.Assertions.assertThat;

class GameDtoShould {

    @Test
    void convertDomainObjectProperly() {
        // GIVEN
        Game actualGame = TestFixtures.a2playersGame();
        GameDto expectedGameDto = buildExpectedGameDto(actualGame);

        // WHEN
        GameDto actualGameDto = GameDto.from(actualGame);

        // THEN
        assertThat(actualGameDto).usingRecursiveComparison().isEqualTo(expectedGameDto);
    }

    private GameDto buildExpectedGameDto(Game game) {
        UserDto player1 = UserDto.from(game.getPlayers().getCreator());
        List<UserDto> players = game.getPlayers().getIndividualPlayers().stream().map(UserDto::from).toList();
        return new GameDto(null, game.getName(), CREATED.toString(), player1, players);
    }
}