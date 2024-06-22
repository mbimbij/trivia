package com.adaptionsoft.games.trivia.infra;

import com.adaptionsoft.games.trivia.domain.event.Event;
import com.adaptionsoft.games.trivia.domain.gamelogs.GameLog;
import com.adaptionsoft.games.trivia.domain.gamelogs.GameLogsRepository;
import com.adaptionsoft.games.trivia.microarchitecture.EventListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameLogsPersister implements EventListener {
    private final GameLogsRepository gameLogsRepository;

    @Override
    public boolean accept(Event event) {
        return true;
    }

    @Override
    public void handle(Event event) {
        gameLogsRepository.addLog(new GameLog(event.getGameId(), event.getStringValue()));
    }

}
