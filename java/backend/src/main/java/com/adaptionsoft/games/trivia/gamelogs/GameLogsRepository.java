package com.adaptionsoft.games.trivia.gamelogs;

import com.adaptionsoft.games.trivia.game.domain.GameId;

import java.util.Collection;

// TODO Introduire / Refacto en `EventStore` ?
public interface GameLogsRepository {
    void addLog(GameLog gameLogs);
    Collection<GameLog> getLogsForGame(GameId id);
}
