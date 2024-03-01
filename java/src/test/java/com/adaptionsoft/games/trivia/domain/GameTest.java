package com.adaptionsoft.games.trivia.domain;


import com.adaptionsoft.games.trivia.domain.event.Event;
import com.adaptionsoft.games.trivia.domain.event.GameCreatedEvent;
import com.adaptionsoft.games.trivia.domain.event.MockEventPublisher;
import com.adaptionsoft.games.trivia.domain.event.PlayerAddedEvent;
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
    public void should_not_differ_from_golden_master() throws IOException {
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
        assertThrows(Players.InvalidNumberOfPlayersException.class, () -> gameFactory.create("player1"));
    }

    @Test
    void cannot_have_multiple_players_with_same_name() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int duplicatesCount = random.nextInt(1, Players.MAX_PLAYER_COUNT) + 1;
            String[] playersNamesWithDuplicates = generatePlayersNamesWithDuplicates(duplicatesCount);
            System.out.println(Arrays.toString(playersNamesWithDuplicates));

            assertThrows(Players.DuplicatePlayerNameException.class, () -> gameFactory.create(playersNamesWithDuplicates));
        }
    }

    private String[] generatePlayersNamesWithDuplicates(int duplicatesCount) {
        if (duplicatesCount < 2 || duplicatesCount > 6) {
            throw new IllegalArgumentException("Duplicates count must be between 2 and 6 but was %d".formatted(duplicatesCount));
        }
        Random random = new Random();
        List<String> playersNames = new ArrayList<>(Players.MAX_PLAYER_COUNT);
        addNonDuplicatePlayerNames(duplicatesCount, playersNames);
        addDuplicatePlayerName(duplicatesCount, playersNames, random);
        return playersNames.toArray(new String[0]);
    }

    private void addNonDuplicatePlayerNames(int duplicatesCount, List<String> playersNames) {
        for (int i = 0; i < Players.MAX_PLAYER_COUNT - duplicatesCount; i++) {
            playersNames.add("player_%s".formatted(i));
        }
    }

    private void addDuplicatePlayerName(int duplicatesCount, List<String> playersNames, Random random) {
        for (int i = 0; i < duplicatesCount; i++) {
            playersNames.add(random.nextInt(playersNames.size() + 1), "duplicate name");
        }
    }

    @Test
    void creation_through_constructor__should_not_raise_any_event() {
        // GIVEN
        String playerName1 = "player1";
        String playerName2 = "player2";
        Player player1 = new Player(playerName1, null, null);
        Player player2 = new Player(playerName2, null, null);
        Players players = new Players(player1, player2);

        // WHEN
        new Game(players, eventPublisher);

        // THEN no domain events are produced
        assertThat(eventPublisher.getEvents()).isEmpty();
    }

    @Test
    void creation_through_factory__should_raise_player_added_event() {
        // GIVEN
        String playerName1 = "player1";
        String playerName2 = "player2";
        Player player1 = new Player(playerName1, null, null);
        Player player2 = new Player(playerName2, null, null);

        // WHEN
        gameFactory.create(playerName1, playerName2);

        // THEN the domain events are produced in the correct order
        List<Event> events = eventPublisher.getEvents();
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
