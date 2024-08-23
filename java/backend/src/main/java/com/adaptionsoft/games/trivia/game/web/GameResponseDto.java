package com.adaptionsoft.games.trivia.game.web;

import com.adaptionsoft.games.trivia.game.domain.Dice;
import com.adaptionsoft.games.trivia.game.domain.Game;
import com.adaptionsoft.games.trivia.game.domain.QuestionsDeck;
import com.adaptionsoft.games.trivia.shared.statemachine.State;
import jakarta.validation.constraints.NotBlank;
import lombok.With;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@With
public record GameResponseDto(
        @NotBlank
        Integer id,
        @NotBlank
        String name,
        @NotBlank
        State state,
        @NotBlank
        int turn,
        @NotBlank
        PlayerDto creator,
        @NotBlank
        Collection<PlayerDto> players,
        @NotBlank
        PlayerDto currentPlayer,
        PlayerDto winner,
        QuestionDto currentQuestion,
        Integer currentRoll,
        String currentCategory,
        AnswerDto currentAnswer
        ) {

    public static GameResponseDto from(Game game) {
        List<PlayerDto> playersDto = game.getPlayersList().stream().map(PlayerDto::from).toList();
        PlayerDto creatorDto = PlayerDto.from(game.getCreator());
        PlayerDto currentPlayerDto = Optional.ofNullable(game.getCurrentPlayer())
                .map(PlayerDto::from)
                .orElse(null);
        PlayerDto winnerDto = Optional.ofNullable(game.getWinner()).map(PlayerDto::from).orElse(null);
        QuestionDto questionDto = QuestionDto.from(game.getCurrentQuestion());
        Integer currentRoll = Optional.ofNullable(game.getCurrentRoll()).map(Dice.Roll::value).orElse(null);
        AnswerDto answerDto = Optional.ofNullable(game.getCurrentAnswer()).map(AnswerDto::from).orElse(null);
        String currentCategory = Optional.ofNullable(game.getCurrentCategory()).map(QuestionsDeck.Category::toString).orElse(null);
        return new GameResponseDto(game.getId().getValue(),
                game.getName(),
                game.getState(),
                game.getTurn(),
                creatorDto,
                playersDto,
                currentPlayerDto,
                winnerDto,
                questionDto,
                currentRoll,
                currentCategory,
                answerDto);
    }

}
