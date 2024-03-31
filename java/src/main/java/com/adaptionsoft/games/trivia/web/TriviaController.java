package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameFactory;
import com.adaptionsoft.games.trivia.domain.GameRepository;
import com.adaptionsoft.games.trivia.domain.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/game")
public class TriviaController {

    private final GameRepository gameRepository;
    private final GameFactory gameFactory;

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

    @PostMapping
    @RequestMapping("/{gameId}/player/{playerId}/join")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDto addPlayerToGame(@PathVariable("gameId") Integer gameId,
                                           @RequestBody UserDto userDto) {
        Game game = gameRepository.getById(gameId).orElseThrow();
        game.addPlayer(new Player(userDto.id(), userDto.name()));
        gameRepository.save(game);
        return GameResponseDto.from(game);
    }

    @PostMapping
    @RequestMapping("/{gameId}/player/{playerId}/start")
    public GameResponseDto startGame(@PathVariable("gameId") Integer gameId,
                                     @PathVariable("playerId") Integer playerId) {
        // TODO tester avec un joueur qui ne fait pas partie du jeu
        // TODO tester avec un joueur / usr qui n'existe nulle part
        Game game = gameRepository.getById(gameId).orElseThrow();
        Player player = game.findPlayerById(playerId).orElseThrow();
        game.startBy(player);
        gameRepository.save(game);
        return GameResponseDto.from(game);
    }

}
