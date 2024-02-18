package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.EventConsoleLogger;
import com.adaptionsoft.games.uglytrivia.event.EventPublisher;
import com.adaptionsoft.games.uglytrivia.event.MockEventPublisher;

import java.util.Arrays;
import java.util.Random;

import static com.adaptionsoft.games.uglytrivia.QuestionInitializationStrategyFactory.Types.PROPERTIES_FILES;

public class GameFactory {

    private GameFactory() {
    }

    public static Game create(String... playersNames) {
        return create(new Random(), playersNames);
    }

    public static Game create(Random rand, String... playersNames) {
        RandomAnsweringStrategy randomAnsweringStrategy = new RandomAnsweringStrategy(rand);
        EventPublisher eventPublisher = new MockEventPublisher();
        Board board = new Board();
        Player[] playersArray = Arrays.stream(playersNames)
                .map(playersName -> new Player(playersName,
                        randomAnsweringStrategy,
                        eventPublisher,
                        board))
                .toArray(Player[]::new);
        eventPublisher.register(new EventConsoleLogger());
        Players playersWrapper = new Players(eventPublisher, playersArray);
        return create(rand,
                board,
                playersWrapper,
                QuestionInitializationStrategyFactory.getInstance(PROPERTIES_FILES),
                eventPublisher
        );
    }

    public static Game create(Random rand,
                              Board board, Players players,
                              QuestionInitializationStrategy questionInitializationStrategy,
                              EventPublisher eventPublisher) {
        questionInitializationStrategy.run();
        return new Game(rand, board, players, eventPublisher);
    }
}
