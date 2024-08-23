package com.adaptionsoft.games.trivia.testkit;

import com.adaptionsoft.games.trivia.game.domain.*;
import com.adaptionsoft.games.trivia.game.domain.QuestionsDeck.Category;
import com.adaptionsoft.games.trivia.game.domain.exception.GameNotFoundException;
import com.adaptionsoft.games.trivia.game.domain.exception.PlayerNotFoundInGameException;
import com.adaptionsoft.games.trivia.game.web.GameController;
import com.adaptionsoft.games.trivia.game.web.GameResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final GameController gameController;

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

    @PutMapping("/{gameId}/players/{playerId}/coinCount/{coinCount}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GameResponseDto setCoinCount(@PathVariable("gameId") Integer gameIdInt,
                                        @PathVariable("playerId") String playerIdString,
                                        @PathVariable("coinCount") Integer coinCount) {
        assert coinCount != null && coinCount >= 0;
        Game game = findGameOrThrow(new GameId(gameIdInt));
        Player player = findPlayerOrThrow(game, new UserId(playerIdString));
        player.setCoinCount(coinCount);
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(game);
        return GameResponseDto.from(game);
    }

    @PutMapping("/{gameId}/dice/{number}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GameResponseDto setLoadedDice(@PathVariable("gameId") int gameIdInt,
                                         @PathVariable("number") int number) {
        Game game = findGameOrThrow(new GameId(gameIdInt));
        game.setDice(new LoadedDice(number));
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(game);
        return GameResponseDto.from(game);
    }

    @PutMapping("/{gameId}/questionDeck")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GameResponseDto setLoadedQuestionDeck(@PathVariable("gameId") int gameIdInt,
                                                 @RequestBody Map<Category, Queue<Question>> loadedQuestionDeck) {
        Game game = findGameOrThrow(new GameId(gameIdInt));
        QuestionsDeck questionsDeck = new QuestionsDeck(loadedQuestionDeck);
        questionsDeck.setShuffler(new DoNothingQuestionsShuffler());
        game.setQuestionsDeck(questionsDeck);
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(game);
        return GameResponseDto.from(game);
    }

    @PutMapping("/{gameId}/currentPlayer/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setCurrentPlayer(@PathVariable("gameId") int gameIdInt,
                                 @PathVariable("userId") String playerIdString) {
        Game game = findGameOrThrow(new GameId(gameIdInt));
        Player player = game.findPlayerById(new UserId(playerIdString)).orElseThrow();
        game.setCurrentPlayer(player);
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(game);
    }

    @PutMapping("/getByIdImplementation/exception")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void throwExceptionWhenCallGetGameById() {
        gameController.setGetByIdImplementation(integer -> {
            throw new RuntimeException("some backend exception");
        });
    }

    @PutMapping("/getByIdImplementation/reset")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetGetGameByIdMethod() {
        gameController.setGetByIdImplementation(gameController::getByIdDefaultImplementation);
    }

    @PutMapping("/{gameId}/playersShuffle/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disablePlayersShuffle(@PathVariable("gameId") int gameIdInt) {
        Game game = findGameOrThrow(new GameId(gameIdInt));
        game.setPlayersShuffler(new DoNothingPlayersShuffler());
        gameRepository.save(game);
        notifyGameUpdatedViaWebsocket(game);
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
