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

@RequiredArgsConstructor
@RestController
@RequestMapping(
        value = "/api/games",
        produces = {
                APPLICATION_JSON_VALUE,
                APPLICATION_PROBLEM_JSON_VALUE
        }
)
@CrossOrigin(origins = "${application.allowed-origins}", methods = {DELETE, GET, POST, OPTIONS})
@Slf4j
public class TriviaController {

    private final GameRepository gameRepository;
    private final GameFactory gameFactory;
    private final PlayerFactory playerFactory;
    private final SimpMessagingTemplate template;
    private final GameLogsRepository gameLogsRepository;

    @GetMapping
    public Collection<GameResponseDto> listGames() {
        return gameRepository.list().stream().map(GameResponseDto::from).toList();
    }

    @Operation(summary = "get a game by its id")
    @ApiResponse(
            responseCode = "200",
            content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GameResponseDto.class)
                    )
            }
    )
    @ApiResponse(
            responseCode = "404",
            description = "game not found",
            content = {
                    @Content(
                            mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                            examples = {
                                    @ExampleObject(value = "{\"type\":\"about:blank\",\"title\":\"Not Found\",\"status\":404,\"instance\":\"/games/777\"}")
                            }
                    )
            }
    )
    @GetMapping("/{gameId}")
    public GameResponseDto getGameById(@PathVariable("gameId") int gameIdInt) {
        GameId gameId = new GameId(gameIdInt);
        return gameRepository.findById(gameId)
                .map(GameResponseDto::from)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @GetMapping
    @RequestMapping("/{gameId}/logs")
    public Collection<GameLog> getGameLogs(@PathVariable("gameId") int gameIdInt) {
        return gameLogsRepository.getLogsForGame(new GameId(gameIdInt));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping("/{gameId}")
    public void deleteGameById(@PathVariable("gameId") int gameIdInt) {
        GameId gameId = new GameId(gameIdInt);
        gameRepository.deleteGameById(gameId);
        notifyGameDeletedViaWebsocket(gameId);
    }

    private void notifyGameDeletedViaWebsocket(GameId gameId) {
        template.convertAndSend("/topic/games/deleted", gameId.getValue());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping("/tests")
    public void deleteTestGames() {
        List<Game> gamesToDelete = gameRepository.list()
                .stream()
                .filter(game ->
                        Set.of("qa-user", "test-user-1", "test-user-2")
                                .contains(game.getCreator().getName())
                )
                .toList();
        gamesToDelete.stream()
                .map(Game::getId)
                .map(Id::getValue)
                .forEach(this::deleteGameById);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGames() {
        List<GameId> gameToBeDeletedIds = gameRepository.list().stream().map(Game::getId).toList();
        gameRepository.deleteAll();
        gameToBeDeletedIds.forEach(this::notifyGameDeletedViaWebsocket);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDto createGame(@RequestBody CreateGameRequestDto requestDto) {
        Game game = gameFactory.create(requestDto.gameName(), playerFactory.fromDto(requestDto.creator()));
        gameRepository.save(game);
        template.convertAndSend("/topic/games/created", GameResponseDto.from(game));
        return GameResponseDto.from(game);
    }

    @PostMapping("/{gameId}/players/{playerId}/join")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDto joinGame(@PathVariable("gameId") Integer gameIdInt,
                                    @PathVariable("playerId") String playerId,
                                    @RequestBody PlayerDto playerDto) {
        if(!Objects.equals(playerId, playerDto.id())){
            throw new PlayerIdMismatchException(playerId, playerDto.id());
        }
        Game game = findGameOrThrow(new GameId(gameIdInt));
        game.addPlayer(playerFactory.fromDto(playerDto));
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(game);
        return GameResponseDto.from(game);
    }

    @PostMapping("/{gameId}/players/{playerId}/start")
    public GameResponseDto startGame(@PathVariable("gameId") Integer gameIdInt,
                                     @PathVariable("playerId") String playerIdString) {
        Game game = findGameOrThrow(new GameId(gameIdInt));
        Player player = findPlayerOrThrow(game, new UserId(playerIdString));
        game.start(player);
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(game);
        return GameResponseDto.from(game);
    }

    @PostMapping("/{gameId}/players/{playerId}/rollDice")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDto rollDice(@PathVariable("gameId") Integer gameIdInt,
                                    @PathVariable("playerId") String playerIdString) {
        Game game = findGameOrThrow(new GameId(gameIdInt));
        Player player = findPlayerOrThrow(game, new UserId(playerIdString));
        game.rollDice(player);
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(game);
        return GameResponseDto.from(game);
    }

    @PostMapping("/{gameId}/players/{playerId}/drawQuestion")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDto drawQuestion(@PathVariable("gameId") Integer gameIdInt,
                                        @PathVariable("playerId") String playerIdString) {
        Game game = findGameOrThrow(new GameId(gameIdInt));
        Player player = findPlayerOrThrow(game, new UserId(playerIdString));
        game.drawQuestion(player);
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(game);
        return GameResponseDto.from(game);
    }

    @PostMapping("/{gameId}/players/{playerId}/answer/{answerCode}")
    public boolean answer(@PathVariable("gameId") Integer gameIdInt,
                                  @PathVariable("playerId") String playerIdString,
                                  @PathVariable("answerCode") AnswerCode answerCode) {
        Game game = findGameOrThrow(new GameId(gameIdInt));
        Player player = findPlayerOrThrow(game, new UserId(playerIdString));
        boolean isAnswerCorrect = game.answerCurrentQuestion(player, answerCode);
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(game);
        return isAnswerCorrect;
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
