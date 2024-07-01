package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.web.GameResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class TestRunnerActor {
    private final RestTemplate restTemplate = new RestTemplate();
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
        deleteGame(TestContext.TEST_GAME_NAME_1);
        deleteGame(TestContext.TEST_GAME_NAME_2);
        deleteGame(TestContext.CREATED_GAME_NAME);
    }

    private void deleteGame(String gameName) {
        Integer gameId = testContext.getGameIdForName(gameName);
        if(gameId != null){
            restTemplate.delete(backendUrlBase + "/games/{gameId}", gameId);
            testContext.removeGameId(gameName);
        }
    }
}
