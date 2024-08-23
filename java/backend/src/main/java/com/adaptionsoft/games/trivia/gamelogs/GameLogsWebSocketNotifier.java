package com.adaptionsoft.games.trivia.gamelogs;

import com.adaptionsoft.games.trivia.shared.microarchitecture.Event;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RequiredArgsConstructor

public class GameLogsWebSocketNotifier implements EventListener {
    private final SimpMessagingTemplate template;

    @Override
    public boolean accept(Event event) {
        return true;
    }

    @Override
    public void handle(Event event) {
        GameLog gameLog = new GameLog(event.getGameId(), event.getStringValue());
        template.convertAndSend("/topic/games/%s/logs".formatted(event.getGameId().getValue()), gameLog);
    }
}
