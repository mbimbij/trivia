package com.adaptionsoft.games.tests;

import com.adaptionsoft.games.E2eTestsSpringConfiguration;
import com.adaptionsoft.games.domain.pageObjects.Backend;
import com.adaptionsoft.games.trivia.game.web.GameResponseDto;
import com.adaptionsoft.games.trivia.game.web.UserDto;
import com.adaptionsoft.games.trivia.game.web.WebConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class BackendTest {
    @Test
    @Disabled
    void can_deserialize_response() {
        RestTemplate restTemplate = new E2eTestsSpringConfiguration().restTemplate(new WebConfig().stateDeserializer());
        Backend backend = new Backend(restTemplate, "http://localhost:8080/api");
        GameResponseDto gameResponseDto = backend.createGame("toto2", new UserDto("1qbO28tbqUaV1gOI6aw71vGhR4J2", "Joseph"));
        System.out.println();
    }
}