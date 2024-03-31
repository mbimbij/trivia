package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameFactory;
import com.adaptionsoft.games.trivia.domain.GameRepository;
import com.adaptionsoft.games.trivia.domain.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static com.adaptionsoft.games.trivia.domain.Game.PlayTurnException.notCurrentPlayerException;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class RestAssuredBasedControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @SpyBean
    private GameRepository gameRepository;
    @Autowired
    TriviaController triviaController;

    @BeforeEach
    void setUp() {
        Mockito.reset(gameRepository);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (cls, charset) -> mapper
        ));
    }

    @SneakyThrows
    @Test
    void business_exception_should_throw_409_conflict() {
        // GIVEN an error is thrown when adding a player
        Game mockGame = Mockito.mock(Game.class);
        Game.AddPlayerInvalidStateException exception = new Game.AddPlayerInvalidStateException(1, Game.State.STARTED);
        Mockito.doThrow(exception).when(mockGame).addPlayer(any());
        Mockito.doReturn(Optional.of(mockGame)).when(gameRepository).getById(anyInt());

        // WHEN a new player tries to join the game
        int newPlayerId = 2;
        UserDto newPlayerDto = new UserDto(newPlayerId, "new player");

        //@formatter:off
        given()
                .filters(
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter()
                )
                .port(port)
                .contentType("application/json")
                .body(newPlayerDto)
        .when()
                .post("/game/{gameId}/player/{playerId}/join", 0, newPlayerId)
        .then()
                .statusCode(409)
                .body("timestamp", notNullValue())
                .body("status", equalTo(409))
                .body("message", notNullValue())
        ;
        //@formatter:on
    }


    private final Player creator = new Player(1, "creator");
    private final Player player2 = new Player(2, "player2");
    @Autowired
    private GameFactory gameFactory;

    // TODO factoriser avec le test précédent
    @SneakyThrows
    @Test
    void invalid_player_turn_exception_should_throw_403_unauthorized() {
        // GIVEN an error is thrown when adding a player
        Game game = gameFactory.create("game", creator, player2);
        gameRepository.save(game);

        // WHEN a player tries to play a turn
        //@formatter:off
        given()
                .filters(
                        new RequestLoggingFilter(),
                        new ResponseLoggingFilter()
                )
                .port(port)
        .when()
                .post("/game/{gameId}/player/{playerId}/playTurn", game.getId(), player2.getId())
        .then()
                .statusCode(403)
//                .body("timestamp", notNullValue())
//                .body("status", equalTo(409))
//                .body("message", notNullValue())
        ;
        //@formatter:on
    }
}