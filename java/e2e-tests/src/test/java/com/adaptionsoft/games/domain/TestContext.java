package com.adaptionsoft.games.domain;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class TestContext {
    public static final String QA_FRONTEND_USER_NAME = "qa-user";
    public static final String QA_BACKEND_LOOKUP_NAME = "qa-user-backend";
    public static final String TEST_USER_ID_1 = "id-test-user-1";
    public static final String TEST_USER_NAME_1 = "test-user-1";
    public static final String TEST_USER_ID_2 = "id-test-user-2";
    public static final String TEST_USER_NAME_2 = "test-user-2";

    public static final String TEST_GAME_NAME_1 = "test-game-1";
    public static final String TEST_GAME_NAME_2 = "test-game-2";
    public static final String CREATED_GAME_NAME = "newGame";

    private final Map<String, Integer> gameIdByName = new HashMap<>();

    public Integer getGameIdForName(String gameName) {
        return gameIdByName.get(gameName);
    }

    public void removeGameId(String gameName) {
        gameIdByName.remove(gameName);
    }

    public void putGameId(String gameName, int gameId) {
        gameIdByName.put(gameName, gameId);
    }

}