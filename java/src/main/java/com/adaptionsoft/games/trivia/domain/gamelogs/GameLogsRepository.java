package com.adaptionsoft.games.trivia.domain.gamelogs;

import com.adaptionsoft.games.trivia.domain.GameId;

import java.util.Collection;

// TODO Introduire / Refacto en `EventStore` ?
public interface GameLogsRepository {
    void addLog(GameLog gameLogs);
    Collection<GameLog> getLogsForGame(GameId id);
}
