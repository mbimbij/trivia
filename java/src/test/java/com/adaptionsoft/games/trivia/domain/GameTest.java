package com.adaptionsoft.games.trivia.domain;


import com.adaptionsoft.games.trivia.domain.Game.State;
import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.infra.EventConsoleLogger;
import lombok.SneakyThrows;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {

    private static final PrintStream stdout = System.out;
    private MockEventPublisher eventPublisher;
    private GameFactory gameFactory;
    private final Player creator = new Player(1, "creator");
    private final Player player2 = new Player(2, "player2");
    // GIVEN
    private Game game;

    @BeforeEach
    void setUp() {
        System.setOut(stdout);
        eventPublisher = new MockEventPublisher();
        eventPublisher.register(new EventConsoleLogger());
        gameFactory = new GameFactory(eventPublisher, new QuestionsLoader());
        game = gameFactory.create("game", creator, player2);
    }

    @Test
    public void should_not_differ_from_golden_master() throws IOException {
        // GIVEN
        redirectStdoutToFile();
        String gold = Files.readString(Paths.get("src/test/resources/gold.txt"));
        int seed = 2;
        Game game = gameFactory.create(
                new Random(seed), "game",
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

    @Nested
    class CreateGame{
        @Test
        void cannot_create_game_without_any_player() {
            assertThrows(Players.InvalidNumberOfPlayersException.class, () -> gameFactory.create("game", new String[0]));
        }

        @Test
        void cannot_create_game_with_more_than_6_players() {
            String[] playersNames = {"player1",
                    "player2",
                    "player3",
                    "player4",
                    "player5",
                    "player6",
                    "player7"};
            assertThrows(Players.InvalidNumberOfPlayersException.class, () -> gameFactory.create("game", playersNames));
        }

        @Test
        void can_create_game_with_1_player() {
            assertThatCode(() -> gameFactory.create("game", "player1")).doesNotThrowAnyException();
        }

        @Test
        void cannot_have_multiple_players_with_same_name() {
            Random random = new Random();
            for (int i = 0; i < 100; i++) {
                int duplicatesCount = random.nextInt(1, Players.MAX_PLAYER_COUNT) + 1;
                String[] playersNamesWithDuplicates = generatePlayersNamesWithDuplicates(duplicatesCount);
                System.out.println(Arrays.toString(playersNamesWithDuplicates));

                assertThrows(Players.DuplicatePlayerNameException.class, () -> gameFactory.create("game", playersNamesWithDuplicates));
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
            eventPublisher.clearEvents();
            String playerName1 = "player1";
            String playerName2 = "player2";
            Player player1 = new Player(playerName1);
            Player player2 = new Player(playerName2);
            Players players = new Players(player1, player2);

            // WHEN
            Game game = new Game("game name", eventPublisher, players, new PlayerTurnOrchestrator(null, null, null));

            // THEN no domain events are produced
            assertThat(eventPublisher.getEvents()).isEmpty();
            assertThat(game.getAndClearUncommittedEvents()).isEmpty();
        }

        @Test
        void creation_through_factory__should_raise_player_added_event() {
            // GIVEN
            eventPublisher.clearEvents();
            String playerName1 = "player1";
            String playerName2 = "player2";
            Player player1 = new Player(playerName1);
            Player player2 = new Player(playerName2);

            // WHEN
            gameFactory.create("game", playerName1, playerName2);

            // THEN the domain events are produced in the correct order
            List<Event> events = eventPublisher.getEvents();
            Assertions.assertArrayEquals(events.toArray(), new Event[]{new PlayerAddedEvent(player1, 1),
                    new PlayerAddedEvent(player2, 2),
                    new GameCreatedEvent(null)});
        }
    }

    @Nested
    class JoinGame{
        @ParameterizedTest
        @EnumSource(value = State.class, names = {"STARTED", "ENDED"})
        void cannot_join_game__when_state_is_not_CREATED(State state) {
            // GIVEN
            Game game = TestFixtures.a1playerGame();
            game.setState(state);

            // WHEN
            assertThatThrownBy(() -> game.addPlayer(new Player("new player")))
                    .isInstanceOf(Game.AddPlayerInvalidStateException.class)
                    .hasMessage("Tried to add player for game=%d with state='%s'".formatted(game.getId(), game.getState()));
        }

        @Test
        void cannot_join_game__when_existing_player_with_same_name() {
            // GIVEN
            Game game = TestFixtures.a1playerGame();

            // WHEN
            assertThatThrownBy(() -> game.addPlayer(new Player("player1")))
                    .isInstanceOf(Players.DuplicatePlayerNameException.class)
                    .hasMessageStartingWith("duplicate player name on player join");
        }

        @Test
        void cannot_join_game__when_max_player_count_reached() {
            // GIVEN
            Game game = TestFixtures.a6playersGame();

            // WHEN
            assertThatThrownBy(() -> game.addPlayer(new Player("player7")))
                    .isInstanceOf(Players.InvalidNumberOfPlayersException.class);
        }
    }
    @Nested
    class StartGame{

        @Test
        void creator_can_start_game() {
            // WHEN
            game.startBy(creator);

            // THEN
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(game.getState()).isEqualTo(State.STARTED);
                softAssertions.assertThat(eventPublisher.getEvents())
                        .containsOnlyOnce(new GameStartedEvent(game.getId()));
            });
        }

        @Test
        void joined_player_cannot_start_game() {
            assertSoftly(softAssertions -> {
                softAssertions.assertThatThrownBy(() -> game.startBy(player2)).isInstanceOf(Game.StartException.class);
                softAssertions.assertThat(game.getState()).isEqualTo(State.CREATED);
            });
        }

        @Test
        void cannot_start_a_game_with_less_than_2_players() {
            // GIVEN a 1 player game
            game = gameFactory.create("game", creator);

            // WHEN
            ThrowableAssert.ThrowingCallable callable = () -> game.startBy(creator);

            // THEN
            assertSoftly(softAssertions -> {
                softAssertions.assertThatThrownBy(callable).isInstanceOf(Game.StartException.class);
                softAssertions.assertThat(game.getState()).isEqualTo(State.CREATED);
            });
        }

        @Test
        @Disabled
        // TODO Implement after Players creation logic is refactored
        void cannot_start_a_game_with_more_than_6_players() {
        }
    }

    @Nested
    class PlayTurn{
        @Test
        void player_other_than_current_should_not_be_able_to_play_turn() {
            assertThatThrownBy(() -> game.playTurnBy(player2)).isInstanceOf(Game.PlayTurnException.class);
        }

        @Test
        void current_player_should_be_able_to_play_turn() {
            assertSoftly(softAssertions -> {
                softAssertions.assertThatCode(() -> game.playTurnBy(creator)).doesNotThrowAnyException();
                // TODO apply refacto 'Hide Delegate'
                softAssertions.assertThat(game.getPlayers().getCurrent()).isEqualTo(player2);
            });
        }
    }
}
