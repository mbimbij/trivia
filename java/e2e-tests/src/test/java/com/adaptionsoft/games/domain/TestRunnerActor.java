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
}
