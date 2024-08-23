package com.adaptionsoft.games.trivia.gamelogs;

import com.adaptionsoft.games.trivia.shared.microarchitecture.Event;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventListener;
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
