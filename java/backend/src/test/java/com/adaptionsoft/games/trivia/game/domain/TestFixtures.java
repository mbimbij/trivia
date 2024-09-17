package com.adaptionsoft.games.trivia.game.domain;

import com.adaptionsoft.games.trivia.game.domain.event.MockEventPublisher;
import com.adaptionsoft.games.trivia.game.infra.EventConsoleLogger;
import com.adaptionsoft.games.trivia.shared.microarchitecture.IdGenerator;

import static com.adaptionsoft.games.trivia.game.domain.AnswerCode.*;
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
        String pathString = "src/test/resources/questions";
        return new QuestionsRepositoryJson(pathString);
    }

    public static Game a1playerGame() {
        eventPublisher.clearEvents();
        return gameFactory.create(GAME_TEST_NAME, player1());
    }

    public static Game a2playersGame() {
        eventPublisher.clearEvents();
        return gameFactory.create(GAME_TEST_NAME, player1(), player2());
    }

    public static Game a6playersGame() {
        eventPublisher.clearEvents();
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
                new AvailableAswers(
                        "Question A",
                        "Question B",
                        "Question C",
                        "Question D"
                ),
                A,
                "explanations");
    }

    public static Question questionTest(int i) {
        return new Question("id-question-test-"+i,
                "question test "+i,
                new AvailableAswers(
                        "Question A",
                        "Question B",
                        "Question C",
                        "Question D"
                ),
                A,
                "explanations "+i);
    }

    public static LoadedQuestionsDeck loadedQuestionsDeck() {
        return new LoadedQuestionsDeck(questionTest());
    }
}
