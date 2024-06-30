package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@Getter
public class TestContext {
    public static final String QA_USER_NAME = "qa-user";
    public static final String ID_TEST_USER_1 = "id-test-user-1";
    public static final String TEST_USER_NAME_1 = "test-user-1";
    public static final String ID_TEST_USER_2 = "id-test-user-2";
    public static final String TEST_USER_NAME_2 = "test-user-2";
    private final Map<String, Integer> gameIdByName = new HashMap<>();
    private final String frontendUrlBase;
    private final String backendUrlBase;
    private UserDto qaUser;
    private Map<String, UserDto> usersByName;
    @Value("${test.qa-user-id}")
    private String qaUserId;
    @Value("${test.qa-user-password}")
    private String qaUserPassword;
    private final UserDto user1 = new UserDto(ID_TEST_USER_1, TEST_USER_NAME_1);
    private final UserDto user2 = new UserDto(ID_TEST_USER_2, TEST_USER_NAME_2);

    @PostConstruct
    void postConstruct() {
        qaUser = new UserDto(qaUserId, QA_USER_NAME);
        usersByName = Map.of(
                TEST_USER_NAME_1, user1,
                TEST_USER_NAME_2, user2,
                QA_USER_NAME, qaUser);
    }

    public int getGameIdForName(String gameName) {
        Assertions.assertThat(gameIdByName).containsKey(gameName);
        return gameIdByName.get(gameName);
    }

    public void deleteGame(String gameName) {
        Integer gameId = gameIdByName.get(gameName);
        if (gameId != null) {
            new RestTemplate().delete(backendUrlBase + "/games/{gameId}", gameId);
            gameIdByName.remove(gameName);
        }
    }

    public void putGameId(String gameName, int gameId) {
        gameIdByName.put(gameName, gameId);
    }

    public UserDto getUserDtoByName(String userName) {
        assertThat(usersByName).containsKey(userName);
        return usersByName.get(userName);
    }
}