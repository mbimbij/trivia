package com.adaptionsoft.games.trivia.game.web;

public record JoinGameRequestDto(Integer pathVariableGameId, String pathVariablePlayerId, PlayerDto playerDto) {
}
