package com.adaptionsoft.games.domain;

import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class BackendActor extends TestActor {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrlBase;

    public BackendActor(@NotBlank String id, @NotBlank String name, String backendUrlBase) {
        super(id, name);
        this.backendUrlBase = backendUrlBase;
    }

    public static BackendActor from(UserDto userDto, String backendUrlBase) {
        return new BackendActor(userDto.id(), userDto.name(), backendUrlBase);
    }

    @Override
    public void join(GameResponseDto game) {
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games/{gameId}/players/{userId}/join",
                new UserDto(this.id, this.name),
                GameResponseDto.class,
                game.id(),
                this.id);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Override
    public void start(GameResponseDto game) {
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games/{gameId}/players/{userId}/start",
                new UserDto(this.id, this.name),
                GameResponseDto.class,
                game.id(),
                this.id);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Override
    public void rollDice() {
        throw new UnsupportedOperationException();
    }
}
