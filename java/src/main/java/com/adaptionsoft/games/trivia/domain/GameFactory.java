package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.infra.EventConsoleLogger;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import com.adaptionsoft.games.trivia.domain.event.GameCreatedEvent;

import java.util.Arrays;
import java.util.Random;

import static com.adaptionsoft.games.trivia.domain.QuestionInitializationStrategyFactory.Types.PROPERTIES_FILES;

public class GameFactory {

    private final EventPublisher eventPublisher;

    public GameFactory(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public Game create(String... playersNames) {
        return create(new Random(), playersNames);
    }

    public Game create(Random rand, String... playersNames) {
        Board board = new Board(12);
        Player[] playersArray = Arrays.stream(playersNames)
                .map(playersName -> new Player(playersName,
                        board,
                        rand))
                .toArray(Player[]::new);
        eventPublisher.register(new EventConsoleLogger());
        Players players = new Players(playersArray);
        eventPublisher.raise(players.getAndClearUncommittedEvents());
        QuestionInitializationStrategyFactory.getInstance(PROPERTIES_FILES).run();

        Game game = new Game(
                players,
                eventPublisher
        );

        eventPublisher.raise(new GameCreatedEvent());
        return game;
    }
}
