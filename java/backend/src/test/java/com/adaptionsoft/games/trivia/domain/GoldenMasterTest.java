package com.adaptionsoft.games.trivia.domain;


import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.infra.EventConsoleLogger;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class GoldenMasterTest {

    private GameFactory gameFactory;
    private PlayerFactory playerFactory;
    private final PrintStream console = System.out;

    @BeforeEach
    void setUp() {
        IdGenerator idGenerator = mock(IdGenerator.class);
        doReturn(1).when(idGenerator).nextId();
        MockEventPublisher eventPublisher = new MockEventPublisher();
        eventPublisher.register(new EventConsoleLogger());
        gameFactory = new GameFactory(idGenerator, eventPublisher, new QuestionsRepositoryTxt("src/main/resources/questions"));
        playerFactory = new PlayerFactory(eventPublisher);
    }

    @AfterEach
    void tearDown() {
        System.setOut(console);
    }

    @Test
    void should_not_differ_from_golden_master() throws IOException {
        // GIVEN
        redirectStdoutToFile();
        String gold = Files.readString(Paths.get("src/test/resources/gold.txt"));
        int seed = 2;
        Player chet = playerFactory.create(new UserId("id-Chet"), "Chet");
        Player pat = playerFactory.create(new UserId("id-Pat"), "Pat");
        Player sue = playerFactory.create(new UserId("id-Sue"), "Sue");
        Player joe = playerFactory.create(new UserId("id-Joe"), "Joe");
        Player vlad = playerFactory.create(new UserId("id-Vlad"), "Vlad");
        Game game = gameFactory.create(new Random(seed),
                "game",
                chet,
                pat,
                sue,
                joe,
                vlad);

        // WHEN
        game.play();

        // THEN
        String lead = Files.readString(Paths.get("src/test/resources/lead.txt"));
        assertEquals(gold, lead);
    }

    @SneakyThrows
    private void redirectStdoutToFile() {
        System.setOut(new PrintStream("src/test/resources/lead.txt"));
    }
}
