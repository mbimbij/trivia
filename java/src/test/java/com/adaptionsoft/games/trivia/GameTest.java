package com.adaptionsoft.games.trivia;


import com.adaptionsoft.games.uglytrivia.*;
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

public class GameTest {

    private static final PrintStream stdout = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(stdout);
    }

    @Test
    public void should_not_differ_from_golden() throws IOException {
        // GIVEN
        redirectStdoutToFile();
        String gold = Files.readString(Paths.get("src/test/resources/gold.txt"));
        int seed = 0;
        Game game = GameFactory.create(
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
                () -> GameFactory.create(
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
                () -> GameFactory.create("player1"));
    }

    @Test
    void cannot_have_multiple_players_with_same_name() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int duplicatesCount = random.nextInt(1, Players.MAX_PLAYER_COUNT) + 1;
            expectDuplicatePlayerNameException(generatePlayersNamesWithDuplicates(duplicatesCount));
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

    private void expectDuplicatePlayerNameException(String... playersNames) {
        System.out.println(Arrays.toString(playersNames));
        assertThrows(Players.DuplicatePlayerNameException.class, () -> GameFactory.create(playersNames));
    }

    @Test
    void game_creation__should_produce_player_added_event() {
        // GIVEN
        Random rand = new Random();
        String playerName1 = "player1";
        String playerName2 = "player2";
        FixedAnsweringStrategy player1AnsweringStratey = new FixedAnsweringStrategy(false);
        Player player1 = new Player(playerName1, player1AnsweringStratey);
        Player player2 = new Player(playerName2, new RandomAnsweringStrategy(rand));

        // WHEN
        Game game = GameFactory.create(rand, player1, player2);

        // THEN player1 is indeed the current player
        assertEquals(player1, game.getCurrentPlayer());

        // THEN the domain events are produced in the correct order
        List<Event> events = game.getUncommittedEvents();
        assertThat(events)
                .usingRecursiveComparison()
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsSubsequence(
                        new PlayerAddedEvent(player1),
                        new PlayerAddedEvent(player2),
                        new GameCreatedEvent()
                );
    }

    @Test
    void player_should_go_to_jail__after_2_wrong_consecutive_answers() {
        // GIVEN
        Random rand = new Random();
        String playerName1 = "player1";
        String playerName2 = "player2";
        FixedAnsweringStrategy player1AnsweringStratey = new FixedAnsweringStrategy(false);
        Player player1 = new Player(playerName1, player1AnsweringStratey);
        Player player2 = new Player(playerName2, new RandomAnsweringStrategy(rand));
        Game game = GameFactory.create(rand, player1, player2);
        assertEquals(player1, game.getCurrentPlayer());

        // WHEN
        game.askQuestionToCurrentPlayer();

        // THEN the domain events are produced in the correct order
        List<Event> events = game.getUncommittedEvents();
        assertThat(events)
                .usingRecursiveComparison()
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsSubsequence(
                        new PlayerAnsweredIncorrectlyEvent(player1, 1),
                        new PlayerAnsweredIncorrectlyEvent(player1, 1),
                        new PlayerSentToPenaltyBoxEvent(player1, 1)
                );
    }
}
