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

    @BeforeEach
    void setUp() {
        IdGenerator idGenerator = mock(IdGenerator.class);
        doReturn(1).when(idGenerator).nextId();
        MockEventPublisher eventPublisher = new MockEventPublisher();
        eventPublisher.register(new EventConsoleLogger());
        gameFactory = new GameFactory(idGenerator, eventPublisher, new QuestionsRepositoryTxt("src/main/resources/questions"));
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }

    @Test
    void should_not_differ_from_golden_master() throws IOException {
        // GIVEN
        redirectStdoutToFile();
        String gold = Files.readString(Paths.get("src/test/resources/gold.txt"));
        int seed = 2;
        final String[] strings = new String[]{"Chet", "Pat", "Sue", "Joe", "Vlad"};
        Game game = gameFactory.create(new Random(seed),
                "game",
                "Chet",
                "Pat",
                "Sue",
                "Joe",
                "Vlad");

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
