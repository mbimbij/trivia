package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.domain.Question;
import com.adaptionsoft.games.trivia.domain.QuestionsDeck;
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

import static com.adaptionsoft.games.domain.TestContext.*;
import static com.adaptionsoft.games.trivia.domain.QuestionsDeck.Category.*;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class Janitor {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String backendUrlBase;
    private final TestContext testContext;

    public void setLoadedDiceForGame(int gameId, int number) {
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(
                backendUrlBase + "/testkit/games/{gameId}/setLoadedDice/{number}",
                null,
                GameResponseDto.class,
                gameId,
                number);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
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
        deleteGame(TEST_GAME_NAME_1, testContext.getGameIdForName(TEST_GAME_NAME_1));
        deleteGame(TEST_GAME_NAME_2, testContext.getGameIdForName(TEST_GAME_NAME_2));
        deleteGame(CREATED_GAME_NAME, testContext.getGameIdForName(CREATED_GAME_NAME));
    }

    private void deleteGame(String gameName, Integer gameId) {
        if (gameId != null) {
            restTemplate.delete(backendUrlBase + "/games/{gameId}", gameId);
            testContext.removeGameId(gameName);
        }
    }

    @SneakyThrows
    public void setLoadedQuestionDeckForGame(Integer gameId) {
        Map<Category, Queue<Question>> loadedQuestionsDeck = new HashMap<>();
        loadedQuestionsDeck.put(GEOGRAPHY, mapper.readValue(Paths.get("src/test/resources/questions-test/Geography.json").toFile(), new TypeReference<>() {
        }));
        loadedQuestionsDeck.put(POP, mapper.readValue(Paths.get("src/test/resources/questions-test/Pop.json").toFile(), new TypeReference<>() {
        }));
        loadedQuestionsDeck.put(SPORTS, mapper.readValue(Paths.get("src/test/resources/questions-test/Sports.json").toFile(), new TypeReference<>() {
        }));
        loadedQuestionsDeck.put(SCIENCE, mapper.readValue(Paths.get("src/test/resources/questions-test/Science.json").toFile(), new TypeReference<>() {
        }));
        restTemplate.put(backendUrlBase + "/testkit/games/{gameId}/setLoadedQuestionDeck", loadedQuestionsDeck,gameId);
    }
}
