package com.adaptionsoft.games.trivia.domain;

import java.util.Map;
import java.util.Queue;

import static com.adaptionsoft.games.trivia.domain.Game.State.CREATED;

public class TestFixtures {

    public static final int GAME_TEST_ID = 1;
    private static final GameId GAME_ID = new GameId(GAME_TEST_ID);

    public static Questions questions() {
        String pathString = "src/test/resources/questions-test";
        return questions(pathString);
    }

    private static Questions questions(String pathString) {
        QuestionsLoader questionsLoader = new QuestionsLoader();
        Map<Questions.Category, Queue<String>> questionsMap = questionsLoader.loadQuestionsFromDirectory(pathString);
        return new Questions(questionsMap);
    }

    public static Game a1playerGame() {
        Players players = new Players(player1());
        Game game = new Game(
                GAME_ID,
                "game name",
                null,
                players,
                new PlayerTurnOrchestrator(null, null, null),
                players.getCurrent(),
                CREATED);
        return game;
    }

    public static Game a2playersGame() {
        Players players = new Players(player1(), player2());
        Game game = new Game(
                GAME_ID,
                "game name",
                null,
                players,
                new PlayerTurnOrchestrator(null, null, null),
                players.getCurrent(),
                CREATED);
        return game;
    }

    public static Game a6playersGame() {
        Players players = new Players(player1(),
                player2(),
                player(3),
                player(4),
                player(5),
                player(6)
        );
        Game game = new Game(
                GAME_ID,
                "game name",
                null,
                players,
                new PlayerTurnOrchestrator(null, null, null),
                players.getCurrent(),
                CREATED);
        return game;
    }

    public static GameId gameId() {
        return new GameId(1);
    }

    public static Player player1() {
        return new Player(new UserId("playerId1"), "player1");
    }

    public static Player player2() {
        return new Player(new UserId("playerId2"), "player2");
    }

    public static Player player(int n) {
        return new Player(new UserId("playerId%d".formatted(n)), "player%d".formatted(n));
    }
}
