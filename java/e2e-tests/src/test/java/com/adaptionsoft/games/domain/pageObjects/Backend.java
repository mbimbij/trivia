package com.adaptionsoft.games.domain.pageObjects;

import com.adaptionsoft.games.trivia.domain.AnswerCode;
import com.adaptionsoft.games.trivia.web.CreateGameRequestDto;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class Backend {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrlBase;

    public Backend(String backendUrlBase) {
        this.backendUrlBase = backendUrlBase;
    }

    public GameResponseDto createGame(String gameName, UserDto userDto) {
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games",
                new CreateGameRequestDto(gameName, userDto),
                GameResponseDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return responseEntity.getBody();
    }

    public void joinGame(int gameId, UserDto userDto) {
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games/{gameId}/players/{userId}/join",
                userDto,
                GameResponseDto.class,
                gameId,
                userDto.id());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    
    public void deleteGame(int gameId) {
        restTemplate.delete(backendUrlBase + "/games/{gameId}", gameId);
    }

    public void startGame(int gameId, @NotBlank String userId) {
        String url = backendUrlBase + "/games/{gameId}/players/{userId}/start";
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(url,
                null,
                GameResponseDto.class,
                gameId,
                userId);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public void rollDice(int gameId, @NotBlank String userId) {
        String url = backendUrlBase + "/games/{gameId}/players/{userId}/rollDice";
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(url,
                null,
                GameResponseDto.class,
                gameId,
                userId);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    public void drawQuestion(int gameId, @NotBlank String userId) {
        String url = backendUrlBase + "/games/{gameId}/players/{userId}/drawQuestion";
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(url,
                null,
                GameResponseDto.class,
                gameId,
                userId);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    public void answerQuestion(int gameId, @NotBlank String userId, AnswerCode answerCode) {
        String url = backendUrlBase + "/games/{gameId}/players/{userId}/answer/{answerCode}";
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(url,
                null,
                GameResponseDto.class,
                gameId,
                userId,
                answerCode);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public void validate(int gameId, @NotBlank String userId) {
        String url = backendUrlBase + "/games/{gameId}/players/{userId}/validate";
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(url,
                null,
                GameResponseDto.class,
                gameId,
                userId);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
