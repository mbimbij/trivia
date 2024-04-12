package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Game;
import lombok.With;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@With
public record GameResponseDto(
        Integer id,
        String name,
        String state,
        UserDto creator,
        Collection<UserDto> players,
        UserDto currentPlayer

        ) {
    public static GameResponseDto from(Game game) {
        List<UserDto> players = game.getPlayersList().stream().map(UserDto::from).toList();
        UserDto creator = UserDto.from(game.getCreator());
        UserDto currentPlayerDto = Optional.ofNullable(game.getCurrentPlayer())
                .map(UserDto::from)
                .orElse(null);
        return new GameResponseDto(game.getId(),
                game.getName(),
                game.getState().toString(),
                creator,
                players,
                currentPlayerDto);
    }

}
