package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.*;
import com.adaptionsoft.games.trivia.domain.exception.GameNotFoundException;
import com.adaptionsoft.games.trivia.domain.exception.PlayerNotFoundInGameException;
import com.adaptionsoft.games.trivia.domain.gamelogs.GameLog;
import com.adaptionsoft.games.trivia.domain.gamelogs.GameLogsRepository;
import com.adaptionsoft.games.trivia.microarchitecture.Id;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * TODO mettre en place du contrôle d'accès pour que uniquement un dev, un testeur ou la CICD puisse accéder à ces endpoints
  */
@RequiredArgsConstructor
@RestController
@RequestMapping(
        value = "/api/testkit/games",
        produces = {
                APPLICATION_JSON_VALUE,
                APPLICATION_PROBLEM_JSON_VALUE
        }
)
@CrossOrigin(origins = "${application.allowed-origins}", methods = {DELETE, GET, POST, OPTIONS})
@Slf4j
public class TestKitController {

    private final GameRepository gameRepository;
    private final GameFactory gameFactory;
    private final PlayerFactory playerFactory;
    private final SimpMessagingTemplate template;
    private final GameLogsRepository gameLogsRepository;

    @PostMapping("/{gameId}/players/{playerId}/goToPenaltyBox")
    public GameResponseDto goToPenaltyBox(@PathVariable("gameId") Integer gameIdInt,
                                  @PathVariable("playerId") String playerIdString) {
        Game game = findGameOrThrow(new GameId(gameIdInt));
        Player player = findPlayerOrThrow(game, new UserId(playerIdString));
        player.goToPenaltyBox();
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(game);
        return GameResponseDto.from(game);
    }

    @PostMapping("/{gameId}/setLoadedDice/{number}")
    public GameResponseDto setLoadedDice(@PathVariable("gameId") int gameIdInt,
                                  @PathVariable("number") int number) {
        Game game = findGameOrThrow(new GameId(gameIdInt));
        game.setDice(new LoadedDice(number));
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(game);
        return GameResponseDto.from(game);
    }

    private void notifyGameUpdatedViaWebsocket(Game game) {
        Integer gameIdInt = game.getId().getValue();
        template.convertAndSend("/topic/games/%d".formatted(gameIdInt),
                GameResponseDto.from(game));
    }

    private Game findGameOrThrow(GameId gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
    }

    private Player findPlayerOrThrow(Game game, UserId playerId) {
        return game.findPlayerById(playerId).orElseThrow(() -> new PlayerNotFoundInGameException(game.getId(), playerId));
    }
}
