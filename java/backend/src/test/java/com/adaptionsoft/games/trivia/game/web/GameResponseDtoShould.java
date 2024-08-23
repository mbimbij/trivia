package com.adaptionsoft.games.trivia.game.web;

import com.adaptionsoft.games.trivia.game.domain.Dice;
import com.adaptionsoft.games.trivia.game.domain.Game;
import com.adaptionsoft.games.trivia.game.domain.QuestionsDeck;
import com.adaptionsoft.games.trivia.game.domain.TestFixtures;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.adaptionsoft.games.trivia.game.domain.GameState.CREATED;
import static com.adaptionsoft.games.trivia.game.domain.TestFixtures.GAME_TEST_ID_INTEGER;
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
        @NotBlank PlayerDto player1 = PlayerDto.from(game.getCreator());
        List<PlayerDto> players = game.getPlayersList().stream().map(PlayerDto::from).toList();
        QuestionDto questionDto = QuestionDto.from(game.getCurrentQuestion());
        Integer value = Optional.ofNullable(game.getCurrentRoll()).map(Dice.Roll::value).orElse(null);
        String category = Optional.ofNullable(value)
                .map(QuestionsDeck.Category::getQuestionCategory)
                .map(QuestionsDeck.Category::toString)
                .orElse(null);
        return new GameResponseDto(GAME_TEST_ID_INTEGER,
                game.getName(),
                CREATED,
                0,
                player1,
                players,
                player1,
                null,
                questionDto,
                value,
                category,
                null);
    }
}