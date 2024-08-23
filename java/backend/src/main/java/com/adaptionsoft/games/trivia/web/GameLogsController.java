package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.*;
import com.adaptionsoft.games.trivia.domain.gamelogs.GameLog;
import com.adaptionsoft.games.trivia.domain.gamelogs.GameLogsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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
public class GameLogsController {

    private final GameLogsRepository gameLogsRepository;

    @GetMapping("/{gameId}/logs")
    public Collection<GameLog> getGameLogs(@PathVariable("gameId") int gameIdInt) {
        return gameLogsRepository.getLogsForGame(new GameId(gameIdInt));
    }
}
