package com.adaptionsoft.games.trivia.game.application;

import com.adaptionsoft.games.trivia.game.domain.Game;
import com.adaptionsoft.games.trivia.game.domain.GameFactory;
import com.adaptionsoft.games.trivia.game.domain.GameRepository;
import com.adaptionsoft.games.trivia.game.domain.Player;
import com.adaptionsoft.games.trivia.gamelogs.GameLog;
import com.adaptionsoft.games.trivia.gamelogs.GameLogsRepository;
import com.adaptionsoft.games.trivia.shared.microarchitecture.IdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;

import static com.adaptionsoft.games.trivia.game.domain.TestFixtures.player1;
import static com.adaptionsoft.games.trivia.game.domain.TestFixtures.player2;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class GreyBoxTests {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameFactory gameFactory;
    @SpyBean
    private IdGenerator idGenerator;
    @Autowired
    private GameLogsRepository gameLogsRepository;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
        Mockito.reset(idGenerator);
    }

    @Test
    void create_game__should_store_logs() {
        // GIVEN
        Player player1 = player1();
        Player player2 = player2();

        // WHEN
        Game game = gameFactory.create("game name", player1, player2);

        // THEN
        Collection<GameLog> gameLogs = gameLogsRepository.getLogsForGame(game.getId());
        assertThat(gameLogs).isNotEmpty();
    }
}
