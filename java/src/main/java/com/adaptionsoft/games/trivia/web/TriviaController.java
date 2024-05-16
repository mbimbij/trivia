package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameFactory;
import com.adaptionsoft.games.trivia.domain.GameRepository;
import com.adaptionsoft.games.trivia.domain.Player;
import com.adaptionsoft.games.trivia.domain.exception.GameNotFoundException;
import com.adaptionsoft.games.trivia.domain.exception.PlayerNotFoundInGameException;
import com.adaptionsoft.games.trivia.domain.gamelogs.GameLog;
import com.adaptionsoft.games.trivia.domain.gamelogs.GameLogsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(
        value = "/games",
        produces = {
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_PROBLEM_JSON_VALUE
        }
)
@CrossOrigin(origins = "${application.allowed-origins}", methods = {DELETE, GET, POST, OPTIONS})
@Slf4j
public class TriviaController {

    private final GameRepository gameRepository;
    private final GameFactory gameFactory;
    private final SimpMessagingTemplate template;
    private final GameLogsRepository gameLogsRepository;
    // TODO créer un channel websocket pour notifier des changements sur la liste des parties

    @PostConstruct
    public void resetGames() {
        gameRepository.deleteAll();
        Player[] game1Players = IntStream.rangeClosed(1, 2).mapToObj(value -> new Player(value, "player-" + value)).toArray(Player[]::new);
        Player[] game2Players = IntStream.rangeClosed(3, 4).mapToObj(value -> new Player(value, "player-" + value)).toArray(Player[]::new);
        Game game1 = gameFactory.create("game-1", game1Players[0], Arrays.copyOfRange(game1Players, 1, game1Players.length));
        Game game2 = gameFactory.create("game-2", game2Players[0], Arrays.copyOfRange(game2Players, 1, game2Players.length));
        gameRepository.save(game1);
        gameRepository.save(game2);
    }

    @GetMapping
    public Collection<GameResponseDto> listGames() {
        return gameRepository.list().stream().map(GameResponseDto::from).toList();
    }

    @Operation(summary = "get a game by its id")
    @ApiResponse(
            responseCode = "200",
            content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GameResponseDto.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "404",
            description = "game not found",
            content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                            examples = {
                                    @ExampleObject(value = "{\"type\":\"about:blank\",\"title\":\"Not Found\",\"status\":404,\"instance\":\"/games/777\"}")
                            }
                    )
            }
    )
    @GetMapping("/{gameId}")
    public GameResponseDto getGameById(@PathVariable("gameId") int gameId) {
        return gameRepository.findById(gameId).map(GameResponseDto::from).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @GetMapping
    @RequestMapping("/{gameId}/logs")
    public Collection<GameLog> getGameLogs(@PathVariable("gameId") int gameId) {
        return gameLogsRepository.getLogsForGame(gameId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping("/{gameId}")
    public void deleteGameById(@PathVariable("gameId") int gameId) {
        gameRepository.deleteGameById(gameId);
        template.convertAndSend("/topic/games/deleted", gameId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGames() {
        resetGames();
        template.convertAndSend("/topic/games", gameRepository.list());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDto createGame(@RequestBody CreateGameRequestDto requestDto) {
        Game game = gameFactory.create(requestDto.gameName(), requestDto.getCreatorAsDomainObject());
        gameRepository.save(game);
        template.convertAndSend("/topic/games/created", GameResponseDto.from(game));
        return GameResponseDto.from(game);
    }

    @PostMapping("/{gameId}/players/{playerId}/join")
    // FIXME map {playerId} path param to method parameter, validate & test
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDto joinGame(@PathVariable("gameId") Integer gameId,
                                    @RequestBody UserDto userDto) {
        Game game = findGameOrThrow(gameId);
        game.addPlayer(new Player(userDto.id(), userDto.name()));
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(gameId, game);
        return GameResponseDto.from(game);
    }

    @PostMapping("/{gameId}/players/{playerId}/start")
    public GameResponseDto startGame(@PathVariable("gameId") Integer gameId,
                                     @PathVariable("playerId") Integer playerId) {
        Game game = findGameOrThrow(gameId);
        Player player = findPlayerOrThrow(game, playerId);
        game.startBy(player);
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(gameId, game);
        return GameResponseDto.from(game);
    }

    @PostMapping("/{gameId}/players/{playerId}/playTurn")
    public GameResponseDto playTurn(@PathVariable("gameId") Integer gameId,
                                    @PathVariable("playerId") Integer playerId) {
        Game game = findGameOrThrow(gameId);
        Player player = findPlayerOrThrow(game, playerId);
        game.playTurnBy(player);
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(gameId, game);
        return GameResponseDto.from(game);
    }

    private void notifyGameUpdatedViaWebsocket(Integer gameId, Game game) {
        template.convertAndSend("/topic/games/%d".formatted(gameId), GameResponseDto.from(game));
    }

    private Game findGameOrThrow(Integer gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
    }

    private Player findPlayerOrThrow(Game game, Integer playerId) {
        return game.findPlayerById(playerId).orElseThrow(() -> new PlayerNotFoundInGameException(game.getId(), playerId));
    }
}
