package com.adaptionsoft.games.trivia.domain;

class QuestionInitializationStrategyFactory {
    public enum Types{
        DUMMY,
        PROPERTIES_FILES
    }

    public static QuestionInitializationStrategy getInstance(Types types) {
        return switch (types) {
            case DUMMY -> new DummyQuestionInitializationStrategy();
            case PROPERTIES_FILES -> new PropertyFileQuestionInitializationStrategy();
        };
    }
}
