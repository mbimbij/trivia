package com.adaptionsoft.games.domain;

import com.microsoft.playwright.WebSocket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class TestContext {

    private final Map<String, Integer> gameIdByName = new HashMap<>();

    @Setter
    @Getter
    private WebSocket gameListPageWebSocket;

    public Integer getGameIdForName(String gameName) {
        return gameIdByName.get(gameName);
    }

    public void removeGameId(String gameName) {
        gameIdByName.remove(gameName);
    }

    public void putGameId(String gameName, int gameId) {
        gameIdByName.put(gameName, gameId);
    }

    public Collection<Integer> listGameIds(){
        return gameIdByName.values();
    }

    public void clearGames() {
        gameIdByName.clear();
    }

}
