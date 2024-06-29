package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.stepdefs.StepsDefs;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class TestContext {
    private final Map<String, GameResponseDto> gamesByName = new HashMap<String, GameResponseDto>();
    private final String frontendUrlBase;
    private final String backendUrlBase;

    public GameResponseDto getGameByName(String gameName) {
        Assertions.assertThat(gamesByName).containsKey(gameName);
        return gamesByName.get(gameName);
    }

    public void deleteGame(GameResponseDto game, StepsDefs stepsDefs) {
        if (game != null) {
            stepsDefs.getRestTemplate().delete(stepsDefs.getBackendUrlBase() + "/games/{gameId}", game.id());
            gamesByName.remove(game.name());
        }
    }

    public void putGame(String gameName1, GameResponseDto game) {
        gamesByName.put(gameName1, game);
    }
}