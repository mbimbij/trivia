package com.adaptionsoft.games.uglytrivia;

import java.util.Random;

public class GameFactory {

    public static Game createDefault(String... playersNames){
        return createWithSeed(new Random(), playersNames);
    }

    public static Game createWithSeed(Random rand, String... playersNames){
        return createWithSeedAndQuestions(rand, new DummyQuestionInitializationStrategy(), playersNames);
    }

    public static Game createWithSeedAndQuestions(Random rand, QuestionInitializationStrategy questionInitializationStrategy, String... playersNames){
        questionInitializationStrategy.run();
        return new Game(rand, new Board(), new Players(playersNames));
    }
}
