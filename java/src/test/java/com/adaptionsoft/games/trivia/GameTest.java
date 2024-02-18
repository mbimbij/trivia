package com.adaptionsoft.games.trivia;


import com.adaptionsoft.games.uglytrivia.*;
import com.adaptionsoft.games.uglytrivia.event.*;
import lombok.SneakyThrows;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {

    private static final PrintStream stdout = System.out;
    private final MockEventPublisher eventPublisher = new MockEventPublisher();
    private final GameFactory gameFactory = new GameFactory(eventPublisher);

    @BeforeEach
    void setUp() {
        System.setOut(stdout);
        eventPublisher.clearEvents();
    }

    @Test
//    @Disabled
    public void should_not_differ_from_golden() throws IOException {
        // GIVEN
        redirectStdoutToFile();
        String gold = Files.readString(Paths.get("src/test/resources/gold.txt"));
        int seed = 2;
        Game game = gameFactory.create(
                new Random(seed),
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

    @Test
    void cannot_have_more_than_6_players() {
        assertThrows(
                Players.InvalidNumberOfPlayersException.class,
                () -> gameFactory.create(
                        "player1",
                        "player2",
                        "player3",
                        "player4",
                        "player5",
                        "player6",
                        "player7"
                )
        );
    }

    @Test
    void cannot_have_less_than_2_players() {
        assertThrows(Players.InvalidNumberOfPlayersException.class,
                () -> gameFactory.create("player1"));
    }

    @Test
    void cannot_have_multiple_players_with_same_name() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int duplicatesCount = random.nextInt(1, Players.MAX_PLAYER_COUNT) + 1;
            String[] playersNames = generatePlayersNamesWithDuplicates(duplicatesCount);
            System.out.println(Arrays.toString(playersNames));
            assertThrows(Players.DuplicatePlayerNameException.class, () -> gameFactory.create(playersNames));
        }
    }

    private String[] generatePlayersNamesWithDuplicates(int duplicatesCount) {
        if (duplicatesCount < 2 || duplicatesCount > 6) {
            throw new IllegalArgumentException("Duplicates count must be between 2 and 6 but was %d".formatted(duplicatesCount));
        }
        Random random = new Random();
        String duplicatePlayerName = "duplicate";
        List<String> playersNames = new ArrayList<>(Players.MAX_PLAYER_COUNT);
        for (int i = 0; i < Players.MAX_PLAYER_COUNT - duplicatesCount; i++) {
            playersNames.add("player%s".formatted(i));
        }
        for (int i = 0; i < duplicatesCount; i++) {
            playersNames.add(random.nextInt(playersNames.size() + 1), duplicatePlayerName);
        }
        return playersNames.toArray(new String[0]);
    }

    @Test
    void game_creation_through_constructor__should_not_raise_player_added_event() {
        // GIVEN
        Random rand = new Random();
        String playerName1 = "player1";
        String playerName2 = "player2";
        RandomAnsweringStrategy randomAnsweringStrategy = new RandomAnsweringStrategy(rand);
        MockEventPublisher mockEventPublisher = eventPublisher;
        Board board = new Board();
        Player player1 = new Player(playerName1, randomAnsweringStrategy, mockEventPublisher, board);
        Player player2 = new Player(playerName2, randomAnsweringStrategy, mockEventPublisher, board);
        mockEventPublisher.register(new EventConsoleLogger());
        Players players = new Players(mockEventPublisher, player1, player2);

        // WHEN
        new Game(rand, board, players, mockEventPublisher);

        // THEN no domain events are produced
        List<Event> events = mockEventPublisher.getEvents();
        assertThat(events).isEmpty();
    }

    @Test
    void game_creation_through_factory__should_raise_player_added_event() {
        // GIVEN
        Random rand = new Random();
        String playerName1 = "player1";
        String playerName2 = "player2";
        RandomAnsweringStrategy randomAnsweringStrategy = new RandomAnsweringStrategy(rand);
        MockEventPublisher mockEventPublisher = eventPublisher;
        Board board = new Board();
        Player player1 = new Player(playerName1, randomAnsweringStrategy, mockEventPublisher, board);
        Player player2 = new Player(playerName2, randomAnsweringStrategy, mockEventPublisher, board);
        mockEventPublisher.register(new EventConsoleLogger());
        Players players = new Players(mockEventPublisher, player1, player2);

        // WHEN
        Game game = gameFactory.create(rand, playerName1, playerName2);

        // THEN the domain events are produced in the correct order
        List<Event> events = mockEventPublisher.getEvents();
        assertThat(events)
                .usingRecursiveComparison()
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsSubsequence(
                        new PlayerAddedEvent(player1, 1),
                        new PlayerAddedEvent(player2, 2),
                        new GameCreatedEvent()
                );
    }

}
