package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.EventConsoleLogger;
import com.adaptionsoft.games.uglytrivia.event.EventPublisher;
import com.adaptionsoft.games.uglytrivia.event.MockEventPublisher;

import java.util.Arrays;
import java.util.Random;

import static com.adaptionsoft.games.uglytrivia.QuestionInitializationStrategyFactory.Types.DUMMY;
import static com.adaptionsoft.games.uglytrivia.QuestionInitializationStrategyFactory.Types.PROPERTIES_FILES;

public class GameFactory {

    private GameFactory() {
    }

    public static Game create(String... playersNames){
        return create(new Random(), playersNames);
    }

    public static Game create(Random rand, String... playersNames){
        RandomAnsweringStrategy randomAnsweringStrategy = new RandomAnsweringStrategy(rand);
        Player[] playersArray = Arrays.stream(playersNames).map(playersName -> new Player(playersName, randomAnsweringStrategy)).toArray(Player[]::new);
        MockEventPublisher eventPublisher = new MockEventPublisher();
        eventPublisher.register(new EventConsoleLogger());
        Players playersWrapper = new Players(eventPublisher, playersArray);
        return create(rand,
                playersWrapper,
                QuestionInitializationStrategyFactory.getInstance(PROPERTIES_FILES),
                eventPublisher
        );
    }

    public static Game create(Random rand,
                              Players players,
                              QuestionInitializationStrategy questionInitializationStrategy,
                              EventPublisher eventPublisher){
        questionInitializationStrategy.run();
        return new Game(rand, new Board(), players, eventPublisher);
    }
}
