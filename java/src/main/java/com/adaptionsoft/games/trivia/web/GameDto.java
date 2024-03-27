package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Game;
import lombok.With;

import java.util.Collection;
import java.util.List;

@With
public record GameDto(
        Integer id,
        String name,
        String state,
        UserDto creator,
        Collection<UserDto> players

) {
    public static GameDto from(Game game) {
        List<UserDto> players = game.getPlayers().getIndividualPlayers().stream().map(UserDto::from).toList();
        UserDto creator = UserDto.from(game.getPlayers().getCreator());
        return new GameDto(game.getId(), game.getName(), game.getState().toString(), creator, players);
    }
}
