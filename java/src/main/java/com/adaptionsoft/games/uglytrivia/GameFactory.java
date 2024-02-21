package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.EventConsoleLogger;
import com.adaptionsoft.games.uglytrivia.event.EventPublisher;
import com.adaptionsoft.games.uglytrivia.event.GameCreatedEvent;

import java.util.Arrays;
import java.util.Random;

import static com.adaptionsoft.games.uglytrivia.QuestionInitializationStrategyFactory.Types.PROPERTIES_FILES;

public class GameFactory {

    private final EventPublisher eventPublisher;

    public GameFactory(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public Game create(String... playersNames) {
        return create(new Random(), playersNames);
    }

    public Game create(Random rand, String... playersNames) {
        RandomAnsweringStrategy randomAnsweringStrategy = new RandomAnsweringStrategy(rand);
        Board board = new Board(12);
        Player[] playersArray = Arrays.stream(playersNames)
                .map(playersName -> new Player(playersName,
                        randomAnsweringStrategy,
                        board,
                        rand))
                .toArray(Player[]::new);
        eventPublisher.register(new EventConsoleLogger());
        Players playersWrapper = PlayersFactory.create(eventPublisher, playersArray);
        QuestionInitializationStrategyFactory.getInstance(PROPERTIES_FILES).run();

        Game game = new Game(
                board,
                playersWrapper,
                eventPublisher
        );

        eventPublisher.raise(new GameCreatedEvent());
        return game;
    }
}
