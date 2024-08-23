package com.adaptionsoft.games.trivia.gamelogs;

import com.adaptionsoft.games.trivia.game.domain.GameId;

public record GameLog(GameId gameId, String value) {

}
