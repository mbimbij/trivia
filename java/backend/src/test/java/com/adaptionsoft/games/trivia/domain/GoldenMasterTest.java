package com.adaptionsoft.games.trivia.domain;


import com.adaptionsoft.games.trivia.domain.Game.State;
import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.domain.exception.*;
import com.adaptionsoft.games.trivia.infra.EventConsoleLogger;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import lombok.SneakyThrows;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.adaptionsoft.games.trivia.domain.AnswerCode.A;
import static com.adaptionsoft.games.trivia.domain.AnswerCode.B;
import static com.adaptionsoft.games.trivia.domain.Game.State.*;
import static com.adaptionsoft.games.trivia.domain.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GoldenMasterTest {

    private static final PrintStream stdout = System.out;
    private MockEventPublisher eventPublisher = new MockEventPublisher();
    private GameFactory gameFactory;

    @BeforeEach
    void setUp() {
        System.setOut(stdout);
        IdGenerator idGenerator = mock(IdGenerator.class);
        doReturn(1).when(idGenerator).nextId();
        eventPublisher = new MockEventPublisher();
        eventPublisher.register(new EventConsoleLogger());
        gameFactory = new GameFactory(idGenerator, eventPublisher, new QuestionsRepositoryTxt("src/main/resources/questions"));
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
