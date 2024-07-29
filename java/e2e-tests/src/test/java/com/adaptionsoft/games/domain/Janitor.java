package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.domain.Question;
import com.adaptionsoft.games.trivia.domain.QuestionsDeck.Category;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
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

import static com.adaptionsoft.games.trivia.domain.QuestionsDeck.Category.*;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class Janitor {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String backendUrlBase;
    private final TestContext testContext;

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
        restTemplate.delete(backendUrlBase + "/games/tests");
        deleteGame(TestProperties.TEST_GAME_NAME_1, testContext.getGameIdForName(TestProperties.TEST_GAME_NAME_1));
        deleteGame(TestProperties.TEST_GAME_NAME_2, testContext.getGameIdForName(TestProperties.TEST_GAME_NAME_2));
        deleteGame(TestProperties.CREATED_GAME_NAME, testContext.getGameIdForName(TestProperties.CREATED_GAME_NAME));
    }

    private void deleteGame(String gameName, Integer gameId) {
        if (gameId != null) {
            restTemplate.delete(backendUrlBase + "/games/{gameId}", gameId);
            testContext.removeGameId(gameName);
        }
    }

    @SneakyThrows
    public void setLoadedQuestionDeckForGame(int gameId) {
        Map<Category, Queue<Question>> loadedQuestionsDeck = new HashMap<>();
        loadedQuestionsDeck.put(GEOGRAPHY, mapper.readValue(Paths.get("src/test/resources/questions-test/Geography.json").toFile(), new TypeReference<>() {
        }));
        loadedQuestionsDeck.put(POP, mapper.readValue(Paths.get("src/test/resources/questions-test/Pop.json").toFile(), new TypeReference<>() {
        }));
        loadedQuestionsDeck.put(SPORTS, mapper.readValue(Paths.get("src/test/resources/questions-test/Sports.json").toFile(), new TypeReference<>() {
        }));
        loadedQuestionsDeck.put(SCIENCE, mapper.readValue(Paths.get("src/test/resources/questions-test/Science.json").toFile(), new TypeReference<>() {
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
}
