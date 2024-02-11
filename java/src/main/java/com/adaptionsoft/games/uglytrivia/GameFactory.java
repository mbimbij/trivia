package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameFactory {

    private GameFactory() {
    }

    public static Game create(String... playersNames){
        return create(new Random(), playersNames);
    }

    public static Game create(Random rand, String... playersNames){
        RandomAnsweringStrategy randomAnsweringStrategy = new RandomAnsweringStrategy(rand);
        Player[] playersArray = Arrays.stream(playersNames).map(playersName -> new Player(playersName, randomAnsweringStrategy)).toArray(Player[]::new);
        return create(rand,
                new DummyQuestionInitializationStrategy(),
                playersArray);
    }

    public static Game create(Random random, Player... players) {
        return create(random,
                new DummyQuestionInitializationStrategy(),
                players);
    }

    public static Game create(Random rand,
                              QuestionInitializationStrategy questionInitializationStrategy,
                              Player... players){
        questionInitializationStrategy.run();
        Players playersWrapper = new Players(players);
        return new Game(rand, new Board(), playersWrapper);
    }
}
