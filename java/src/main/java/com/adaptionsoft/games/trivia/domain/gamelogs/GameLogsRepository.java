package com.adaptionsoft.games.trivia.domain.gamelogs;

import java.util.Collection;

// TODO Introduire / Refacto en `EventStore` ?
public interface GameLogsRepository {
    void addLog(GameLog gameLogs);
    Collection<GameLog> getLogsForGame(Integer id);
}
