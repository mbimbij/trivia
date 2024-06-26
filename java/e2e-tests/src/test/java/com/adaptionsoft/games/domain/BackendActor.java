package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.web.CreateGameRequestDto;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class BackendActor extends TestActor {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrlBase;
    private final TestContext testContext;

    public BackendActor(@NotBlank String id, @NotBlank String name, String backendUrlBase, TestContext testContext) {
        super(id, name);
        this.backendUrlBase = backendUrlBase;
        this.testContext = testContext;
    }

    public GameResponseDto createGame(String gameName) {
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games",
                new CreateGameRequestDto(gameName, new UserDto(this.id, this.name)),
                GameResponseDto.class,
                this.id);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        GameResponseDto gameResponseDto = responseEntity.getBody();
        testContext.putGameId(gameName, Objects.requireNonNull(gameResponseDto).id());
        return gameResponseDto;
    }

    @Override
    public void join(int gameId) {
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games/{gameId}/players/{userId}/join",
                new UserDto(this.id, this.name),
                GameResponseDto.class,
                gameId,
                this.id);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Override
    public void start(int gameId) {
        String url = backendUrlBase + "/games/{gameId}/players/{userId}/start";
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(url,
                new UserDto(this.id, this.name),
                GameResponseDto.class,
                gameId,
                this.id);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Override
    public void rollDice() {
        throw new UnsupportedOperationException();
    }

    public void deleteGame(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        restTemplate.delete(backendUrlBase + "/games/{gameId}", gameId);
        testContext.removeGameId(gameName);
    }
}
