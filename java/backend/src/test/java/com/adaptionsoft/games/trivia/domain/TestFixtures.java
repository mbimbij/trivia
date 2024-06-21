package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.MockEventPublisher;
import com.adaptionsoft.games.trivia.infra.EventConsoleLogger;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;

import java.util.Map;

import static com.adaptionsoft.games.trivia.domain.AnswerCode.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class TestFixtures {

    public static final int GAME_TEST_ID_INTEGER = 1;
    public static final GameId GAME_TEST_ID = new GameId(GAME_TEST_ID_INTEGER);
    private static final String GAME_TEST_NAME = "game name";
    private static MockEventPublisher eventPublisher;
    private static final GameFactory gameFactory = gameFactory();
    private static final PlayerFactory playerFactory = playerFactory();

    public static PlayerFactory playerFactory() {
        return new PlayerFactory(eventPublisher());
    }

    public static GameFactory gameFactory() {
        return new GameFactory(idGenerator(), eventPublisher(), questionsRepository());
    }

    private static IdGenerator idGenerator() {
        IdGenerator mock = mock(IdGenerator.class);
        doReturn(GAME_TEST_ID_INTEGER).when(mock).nextId();
        return mock;
    }

    public static QuestionsDeck questions() {
        QuestionsRepository questionsLoader = questionsRepository();
        return questionsLoader.getQuestions();
    }

    private static QuestionsRepository questionsRepository() {
        String pathString = "src/test/resources/questions-test-json";
        return new QuestionsRepositoryJson(pathString);
    }

    public static Game a1playerGame() {
        return gameFactory.create(GAME_TEST_NAME, player1());
    }

    public static Game a2playersGame() {
        return gameFactory.create(GAME_TEST_NAME, player1(), player2());
    }

    public static Game a6playersGame() {
        return gameFactory.create(GAME_TEST_NAME, player1(),
                player2(),
                player(3),
                player(4),
                player(5),
                player(6));
    }

    public static Player player1() {
        return player(1);
    }

    public static Player player2() {
        return player(2);
    }

    public static Player player(int n) {
        return playerFactory.create(new UserId("id-player%d".formatted(n)), "player%d".formatted(n));
    }

    public static MockEventPublisher eventPublisher() {
        if (eventPublisher == null) {
            eventPublisher = new MockEventPublisher();
            eventPublisher.register(new EventConsoleLogger());
        }
        return eventPublisher;
    }

    public static Question questionTest() {
        return new Question("id-question-test",
                "question test",
                Map.of(
                        A, "Question A",
                        B, "Question B",
                        C, "Question C",
                        D, "Question D"
                ), A);
    }

    public static LoadedQuestionsDeck loadedQuestionsDeck() {
        return new LoadedQuestionsDeck(questionTest());
    }
}
