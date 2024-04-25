package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameFactory;
import com.adaptionsoft.games.trivia.domain.GameRepository;
import com.adaptionsoft.games.trivia.domain.Player;
import com.adaptionsoft.games.trivia.domain.exception.GameNotFoundException;
import com.adaptionsoft.games.trivia.domain.exception.PlayerNotFoundInGameException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "${application.allowed-origins}")
//@CrossOrigin
public class TriviaController {

    private final GameRepository gameRepository;
    private final GameFactory gameFactory;

    @PostConstruct
    public void postConstruct() {
        Game game1 = gameFactory.create("game-1", "player-1", "player-2", "player-3");
        Game game2 = gameFactory.create("game-2", "player-4", "player-5", "player-6");
        gameRepository.save(game1);
        gameRepository.save(game2);
    }

    @GetMapping
    public Collection<GameResponseDto> listGames() {
        return gameRepository.list().stream().map(GameResponseDto::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDto createGame(@RequestBody CreateGameRequestDto requestDto) {
        Game game = gameFactory.create(requestDto.gameName(), requestDto.toDomain());
        gameRepository.save(game);
        return GameResponseDto.from(game);
    }

    @PostMapping("/{gameId}/players/{playerId}/join")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDto addPlayerToGame(@PathVariable("gameId") Integer gameId,
                                           @RequestBody UserDto userDto) {
        Game game = findGameOrThrow(gameId);
        game.addPlayer(new Player(userDto.id(), userDto.name()));
        gameRepository.save(game);
        return GameResponseDto.from(game);
    }

    @PostMapping("/{gameId}/players/{playerId}/start")
    public GameResponseDto startGame(@PathVariable("gameId") Integer gameId,
                                     @PathVariable("playerId") Integer playerId) {
        Game game = findGameOrThrow(gameId);
        Player player = findPlayerOrThrow(game, playerId);
        game.startBy(player);
        gameRepository.save(game);
        return GameResponseDto.from(game);
    }

    @PostMapping("/{gameId}/players/{playerId}/playTurn")
    public GameResponseDto playTurn(@PathVariable("gameId") Integer gameId,
                                     @PathVariable("playerId") Integer playerId) {
        Game game = findGameOrThrow(gameId);
        Player player = findPlayerOrThrow(game, playerId);
        game.playTurnBy(player);
        gameRepository.save(game);
        return GameResponseDto.from(game);
    }

    private Game findGameOrThrow(Integer gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
    }

    private Player findPlayerOrThrow(Game game, Integer playerId) {
        return game.findPlayerById(playerId).orElseThrow(() -> new PlayerNotFoundInGameException(game.getId(), playerId));
    }
}
