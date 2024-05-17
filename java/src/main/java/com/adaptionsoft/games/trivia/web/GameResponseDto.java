package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Game;
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
        String state,
        @NotBlank
        int turn,
        @NotBlank
        UserDto creator,
        @NotBlank
        Collection<UserDto> players,
        @NotBlank
        UserDto currentPlayer,
        UserDto winner
) {

    public GameResponseDto(@NotBlank Integer id,
                           @NotBlank String name,
                           @NotBlank String state,
                           @NotBlank
                           int turn,
                           @NotBlank
                           UserDto creator,
                           @NotBlank
                           Collection<UserDto> players,
                           @NotBlank
                           UserDto currentPlayer) {
        this(id,
                name,
                state,
                turn,
                creator,
                players,
                currentPlayer,
                null);
    }

    public static GameResponseDto from(Game game) {
        List<UserDto> playersDto = game.getPlayersList().stream().map(UserDto::from).toList();
        UserDto creatorDto = UserDto.from(game.getCreator());
        UserDto currentPlayerDto = Optional.ofNullable(game.getCurrentPlayer())
                .map(UserDto::from)
                .orElse(null);
        UserDto winnerDto = Optional.ofNullable(game.getWinner()).map(UserDto::from).orElse(null);
        return new GameResponseDto(game.getId(),
                game.getName(),
                game.getState().toString(),
                game.getTurn(),
                creatorDto,
                playersDto,
                currentPlayerDto,
                winnerDto);
    }

}
