package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.game.domain.Question;
import com.adaptionsoft.games.trivia.game.domain.QuestionsDeck.Category;
import com.adaptionsoft.games.trivia.game.web.GameResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import static com.adaptionsoft.games.trivia.game.domain.QuestionsDeck.Category.*;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class Janitor {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String backendUrlBase;
    private final TestContext testContext;
    private boolean backendIsUpVerified = false;

    public void setLoadedDiceForGame(int gameId, int number) {
        restTemplate.put(
                backendUrlBase + "/testkit/games/{gameId}/dice/{number}",
                null,
                gameId,
                number);
    }

    public void putQaUserInPenaltyBox(int gameId, String qaUserId) {
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/testkit/games/{gameId}/players/{playerId}/goToPenaltyBox",
                null,
                GameResponseDto.class,
                gameId,
                qaUserId);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public void deleteTestGames() {
        testContext.listGameIds().forEach(this::deleteGame);
        testContext.clearGames();
    }

    private void deleteGame(Integer gameId) {
        restTemplate.delete(backendUrlBase + "/games/{gameId}", gameId);
    }

    @SneakyThrows
    public void setLoadedQuestionDeckForGame(int gameId) {
        Map<Category, Queue<Question>> loadedQuestionsDeck = new HashMap<>();
        loadedQuestionsDeck.put(GEOGRAPHY, mapper.readValue(Paths.get("src/main/resources/questions-test/Geography.json").toFile(), new TypeReference<>() {
        }));
        loadedQuestionsDeck.put(POP, mapper.readValue(Paths.get("src/main/resources/questions-test/Pop.json").toFile(), new TypeReference<>() {
        }));
        loadedQuestionsDeck.put(SPORTS, mapper.readValue(Paths.get("src/main/resources/questions-test/Sports.json").toFile(), new TypeReference<>() {
        }));
        loadedQuestionsDeck.put(SCIENCE, mapper.readValue(Paths.get("src/main/resources/questions-test/Science.json").toFile(), new TypeReference<>() {
        }));
        restTemplate.put(backendUrlBase + "/testkit/games/{gameId}/questionDeck", loadedQuestionsDeck, gameId);
    }

    public void setCoinCount(int gameId, String playerId, int coinCount) {
        restTemplate.put(backendUrlBase + "/testkit/games/{gameId}/players/{playerId}/coinCount/{coinCount}",
                null,
                gameId,
                playerId,
                coinCount);
    }

    public void setCurrentPlayer(int gameId, String playerId) {
        restTemplate.put(backendUrlBase + "/testkit/games/{gameId}/currentPlayer/{playerId}",
                null,
                gameId,
                playerId);
    }

    public void throwExceptionWhenCallGetGameById() {
        restTemplate.put(backendUrlBase + "/testkit/games/getByIdImplementation/exception",null);
    }

    public void resetGetGameByIdMethodBehaviour() {
        restTemplate.put(backendUrlBase + "/testkit/games/getByIdImplementation/reset",null);
    }

    public void throwExceptionWhenCallCreateGame() {
        restTemplate.put(backendUrlBase + "/testkit/games/createGameImplementation/exception",null);
    }

    public void resetCreateGameMethodBehaviour() {
        restTemplate.put(backendUrlBase + "/testkit/games/createGameImplementation/reset",null);
    }

    public void throwExceptionWhenCallJoin() {
        restTemplate.put(backendUrlBase + "/testkit/games/joinImplementation/exception",null);
    }

    public void resetJoinMethodBehaviour() {
        restTemplate.put(backendUrlBase + "/testkit/games/joinImplementation/reset",null);
    }

    public void disablePlayersShuffling(int gameId) {
        restTemplate.put(backendUrlBase + "/testkit/games/{gameId}/playersShuffle/disable",
                null,
                gameId);
    }

    public void verifyBackendIsUp() {
        if (!backendIsUpVerified) {
            try {
                ResponseEntity<Map> responseEntity = restTemplate.getForEntity(backendUrlBase + "/actuator/health", Map.class);
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            } catch (Exception e) {
                System.err.println("************************************");
                System.err.println("Backend is not up. Ending tests");
                System.err.println("************************************");
                System.exit(-1);
            }
            backendIsUpVerified = true;
        }
    }
}
