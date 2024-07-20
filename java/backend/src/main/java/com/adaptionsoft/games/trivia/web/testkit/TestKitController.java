package com.adaptionsoft.games.trivia.web.testkit;

import com.adaptionsoft.games.trivia.domain.*;
import com.adaptionsoft.games.trivia.domain.QuestionsDeck.Category;
import com.adaptionsoft.games.trivia.domain.exception.GameNotFoundException;
import com.adaptionsoft.games.trivia.domain.exception.PlayerNotFoundInGameException;
import com.adaptionsoft.games.trivia.domain.gamelogs.GameLogsRepository;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Queue;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Endpoints utilisés pour les tests "e2e" / "composant"
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
    private final SimpMessagingTemplate template;

    @PostMapping("/{gameId}/players/{playerId}/goToPenaltyBox")
    public GameResponseDto goToPenaltyBox(@PathVariable("gameId") Integer gameIdInt,
                                          @PathVariable("playerId") String playerIdString) {
        Game game = findGameOrThrow(new GameId(gameIdInt));
        Player player = findPlayerOrThrow(game, new UserId(playerIdString));
        player.goToPenaltyBox();
        player.setState(PlayerState.IN_PENALTY_BOX);
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

    @PutMapping("/{gameId}/setLoadedQuestionDeck")
    public GameResponseDto setLoadedQuestionDeck(@PathVariable("gameId") int gameIdInt,
    @RequestBody Map<Category, Queue<Question>> loadedQuestionDeck) {
        Game game = findGameOrThrow(new GameId(gameIdInt));
        game.setQuestionsDeck(new QuestionsDeck(loadedQuestionDeck));
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
