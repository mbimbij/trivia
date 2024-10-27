package com.adaptionsoft.games.trivia.game.web;

import lombok.With;

@With
public record JoinGameRequestDto(Integer pathVariableGameId, String pathVariablePlayerId, PlayerDto playerDto) {
}
