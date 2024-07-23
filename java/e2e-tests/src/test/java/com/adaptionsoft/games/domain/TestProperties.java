package com.adaptionsoft.games.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "test")
@Data
public class TestProperties {
    public static final String QA_FRONTEND_USER_NAME = "qa-user";
    public static final String QA_BACKEND_LOOKUP_NAME = "qa-user-backend";
    public static final String TEST_USER_ID_1 = "id-test-user-1";
    public static final String TEST_USER_NAME_1 = "test-user-1";
    public static final String TEST_USER_ID_2 = "id-test-user-2";
    public static final String TEST_USER_NAME_2 = "test-user-2";
    public static final String TEST_GAME_NAME_1 = "test-game-1";
    public static final String TEST_GAME_NAME_2 = "test-game-2";
    public static final String CREATED_GAME_NAME = "newGame";
    private String qaUserId;
    private String qaUserEmail;
    private String qaUserPassword;
    private String frontendUrlBase;
    private String backendUrlBase;
}
