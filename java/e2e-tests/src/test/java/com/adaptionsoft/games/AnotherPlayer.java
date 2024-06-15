package com.adaptionsoft.games;

import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class AnotherPlayer extends TestPlayer {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrlBase;

    AnotherPlayer(@NotBlank String id, @NotBlank String name, String backendUrlBase) {
        super(id, name);
        this.backendUrlBase = backendUrlBase;
    }

    public static AnotherPlayer from(UserDto userDto, String backendUrlBase) {
        return new AnotherPlayer(userDto.id(), userDto.name(), backendUrlBase);
    }

    @Override
    void join(GameResponseDto game) {
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games/{gameId}/players/{userId}/join",
                new UserDto(this.id, this.name),
                GameResponseDto.class,
                game.id(),
                this.id);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Override
    void start(GameResponseDto game) {
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games/{gameId}/players/{userId}/start",
                new UserDto(this.id, this.name),
                GameResponseDto.class,
                game.id(),
                this.id);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
