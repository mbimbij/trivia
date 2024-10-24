package com.adaptionsoft.games.trivia.game.web;

import com.adaptionsoft.games.trivia.game.domain.Game;
import com.adaptionsoft.games.trivia.game.domain.GameRepository;
import com.adaptionsoft.games.trivia.game.domain.GameState;
import com.adaptionsoft.games.trivia.game.domain.TestFixtures;
import com.adaptionsoft.games.trivia.game.domain.exception.InvalidGameStateException;
import jakarta.validation.constraints.NotBlank;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Optional;

import static com.adaptionsoft.games.trivia.game.domain.TestFixtures.player2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class TestRestTemplateBasedControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @SpyBean
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        Mockito.reset(gameRepository);
    }

    @SneakyThrows
    @Test
    void handle_errors() {
        // GIVEN an error is thrown when adding a player
        Game mockGame = Mockito.mock(Game.class);
        InvalidGameStateException exception = new InvalidGameStateException(TestFixtures.GAME_TEST_ID, GameState.STARTED, "add player");
        Mockito.doThrow(exception).when(mockGame).addPlayer(any());

        Mockito.doReturn(Optional.of(mockGame)).when(gameRepository).findById(any());

        // WHEN a new player tries to join the game
        @NotBlank PlayerDto newPlayerDto = PlayerDto.from(player2());
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                "/api/games/%d/players/%s/join".formatted(0, newPlayerDto.id()),
                newPlayerDto,
                Map.class);

        // THEN the http status code is 409
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        // AND the body is formatted as expected
        Map<String, Object> body = responseEntity.getBody();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(body).containsKey("timestamp");
            softAssertions.assertThat(body).containsKey("message");
            softAssertions.assertThat(body).containsKey("status");
        });
        System.out.println();
    }

}