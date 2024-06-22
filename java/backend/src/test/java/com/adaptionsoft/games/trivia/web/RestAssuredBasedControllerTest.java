package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.exception.InvalidGameStateException;
import com.adaptionsoft.games.trivia.domain.exception.PlayTurnException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.reset;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class RestAssuredBasedControllerTest {
    private RequestSpecification requestSpec;
    @LocalServerPort
    private int port;
    @Autowired
    private ObjectMapper mapper;
    @SpyBean
    private TriviaController triviaController;

    @BeforeEach
    void setUp() {
        reset(triviaController);
        RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (cls, charset) -> mapper
        ));
        requestSpec = given()
                .filters(
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter()
                ).port(port)
        ;
    }

    @SneakyThrows
    @Test
    void business_exception_should_throw_409_conflict() {
        // GIVEN an error is thrown when adding a player
        Mockito.doThrow(new InvalidGameStateException(null, null, "add player"))
                .when(triviaController)
                .joinGame(anyInt(), anyString(), any(PlayerDto.class));

        // WHEN a new player tries to join the game
        //@formatter:off
        requestSpec
                .contentType("application/json")
                .body(new UserDto(null, null))
        .when()
                .post("/api/games/{gameId}/players/{playerId}/join", 0, 0)
        .then()
                .statusCode(409)
                .body("timestamp", notNullValue())
                .body("status", equalTo(409))
                .body("message", notNullValue())
        ;
        //@formatter:on
    }

    @SneakyThrows
    @Test
    @Disabled("tester pour les autres actions utilisateur: lancer un dé, piocher une carte, répondre à une question, etc.")
    void invalid_player_turn_exception_should_throw_403_unauthorized() {
        // GIVEN an error is thrown when adding a player
        Mockito.doThrow(PlayTurnException.notCurrentPlayerException(null, null, null))
                .when(triviaController);

        // WHEN a player tries to play a turn
        //@formatter:off
        requestSpec
        .when()
                .post("/api/games/{gameId}/players/{playerId}/playTurn", 0, 0)
        .then()
                .statusCode(403)
        ;
        //@formatter:on
    }
}