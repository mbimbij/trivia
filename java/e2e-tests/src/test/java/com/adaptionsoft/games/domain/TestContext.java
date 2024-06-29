package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.stepdefs.StepsDefs;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class TestContext {
    private final Map<String, GameResponseDto> gamesByName = new HashMap<String, GameResponseDto>();
    private final String frontendUrlBase;
    private final String backendUrlBase;
    @Getter
    private UserDto qaUser;
    @Getter
    private Map<String, UserDto> usersByName;
    @Value("${test.qa-user-id}")
    private String qaUserId;
    @Value("${test.qa-user-password}")
    private String qaUserPassword;
    private final String userName1 = "test-user-1";
    private final String userName2 = "test-user-2";
    private final String qaUserName = "qa-user";
    private final UserDto user1 = new UserDto("id-test-user-1", userName1);
    private final UserDto user2 = new UserDto("id-test-user-2", userName2);

    @PostConstruct
    void postConstruct() {
        qaUser = new UserDto(qaUserId, qaUserName);
        usersByName = Map.of(
                userName1, user1,
                userName2, user2,
                qaUserName, qaUser);
    }

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

    public UserDto getUserDtoByName(String userName, StepsDefs stepsDefs) {
        assertThat(stepsDefs.getUsersByName()).containsKey(userName);
        return stepsDefs.getUsersByName().get(userName);
    }
}