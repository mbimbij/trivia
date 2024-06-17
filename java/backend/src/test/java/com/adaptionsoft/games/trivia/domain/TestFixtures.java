package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.MockEventPublisher;
import com.adaptionsoft.games.trivia.infra.EventConsoleLogger;

import static com.adaptionsoft.games.trivia.domain.Game.State.CREATED;

public class TestFixtures {

    public static final int GAME_TEST_ID = 1;
    private static final GameId GAME_ID = new GameId(GAME_TEST_ID);
    private static MockEventPublisher eventPublisher;

    public static Questions questions() {
        String pathString = "src/test/resources/questions-test-json";
        QuestionsRepository questionsLoader = new QuestionsRepositoryTxt(pathString);
        return questionsLoader.getQuestions();
    }

    public static Game a1playerGame() {
        Players players = new Players(eventPublisher, player1());
        return new Game(
                GAME_ID,
                "game name",
                null,
                players,
                new PlayerTurnOrchestrator(eventPublisher, null, null, null),
                players.getCurrent(),
                CREATED,
                questions());
    }

    public static Game a2playersGame() {
        Players players = new Players(eventPublisher, player1(), player2());
        return new Game(
                GAME_ID,
                "game name",
                null,
                players,
                new PlayerTurnOrchestrator(eventPublisher, null, null, null),
                players.getCurrent(),
                CREATED,
                questions());
    }

    public static Game a6playersGame() {
        Players players = new Players(eventPublisher, player1(),
                player2(),
                player(3),
                player(4),
                player(5),
                player(6)
        );
        return new Game(
                GAME_ID,
                "game name",
                null,
                players,
                new PlayerTurnOrchestrator(eventPublisher, null, null, null),
                players.getCurrent(),
                CREATED,
                questions());
    }

    public static GameId gameId() {
        return new GameId(1);
    }

    public static Player player1() {
        return new Player(getEventPublisher(), new UserId("playerId1"), "player1");
    }

    public static Player player2() {
        return new Player(getEventPublisher(), new UserId("playerId2"), "player2");
    }

    public static Player player(int n) {
        return new Player(getEventPublisher(), new UserId("playerId%d".formatted(n)), "player%d".formatted(n));
    }

    public static MockEventPublisher getEventPublisher() {
        if (eventPublisher == null) {
            eventPublisher = new MockEventPublisher();
            eventPublisher.register(new EventConsoleLogger());
        }
        return eventPublisher;
    }
}
