package com.adaptionsoft.games.trivia.infra;

import com.adaptionsoft.games.trivia.domain.GameId;
import com.adaptionsoft.games.trivia.domain.gamelogs.GameLog;
import com.adaptionsoft.games.trivia.domain.gamelogs.GameLogsRepository;

import java.util.*;

public class GameLogsRepositoryInMemory implements GameLogsRepository {
    private final Map<GameId, Collection<GameLog>> gameLogs = new HashMap<>();

    @Override
    public void addLog(GameLog gameLog) {
        this.gameLogs.putIfAbsent(gameLog.gameId(),new ArrayList<>());
        this.gameLogs.get(gameLog.gameId()).add(gameLog);
    }

    @Override
    public Collection<GameLog> getLogsForGame(GameId id) {
        return gameLogs.getOrDefault(id, Collections.emptyList());
    }
}
