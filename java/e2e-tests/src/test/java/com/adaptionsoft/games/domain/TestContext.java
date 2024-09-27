package com.adaptionsoft.games.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class TestContext {

    private final Map<String, Integer> gameIdByName = new HashMap<>();
    @Getter
    @Setter
    private boolean userRenamed = false;

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
