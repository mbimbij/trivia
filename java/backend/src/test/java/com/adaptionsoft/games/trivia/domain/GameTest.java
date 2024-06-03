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

import static com.adaptionsoft.games.trivia.domain.Game.State.*;
import static com.adaptionsoft.games.trivia.domain.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameTest {

    private static final PrintStream stdout = System.out;
    private MockEventPublisher eventPublisher;
    private GameFactory gameFactory;
    private final Player player1 = player1();
    private final Player player2 = player2();
    // GIVEN
    private Game game;

    @BeforeEach
    void setUp() {
        System.setOut(stdout);
        IdGenerator idGenerator = new IdGenerator();
        eventPublisher = new MockEventPublisher();
        eventPublisher.register(new EventConsoleLogger());
        gameFactory = new GameFactory(idGenerator, eventPublisher, new QuestionsLoader());
        game = gameFactory.create("game", player1, player2);
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

    @Test
    void should_not_add_duplicate_in_set() {
        // GIVEN
        Set<Game> games = new HashSet<>();
        games.add(game);

        // WHEN
        game.playTurnBy(game.getCurrentPlayer());
        games.add(game);

        // THEN
        assertThat(games).hasSize(1);
    }

    @Nested
    class CreateGame {
        @Test
        void cannot_create_game_without_any_player() {
//            assertThrows(Players.Nu.class, () -> gameFactory.create("game"));
        }

        @Test
        void cannot_create_game_with_more_than_6_players() {
            assertThrows(InvalidNumberOfPlayersException.class, () -> gameFactory.create("game",
                    "player1",
                    "player2",
                    "player3",
                    "player4",
                    "player5",
                    "player6",
                    "player7"));
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
                String creatorName = playersNamesWithDuplicates[0];
                String[] otherPlayersNames = Arrays.copyOfRange(playersNamesWithDuplicates, 1, playersNamesWithDuplicates.length);
                assertThrows(DuplicatePlayerNameException.class, () -> gameFactory.create("game", creatorName, otherPlayersNames));
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
            Players players = new Players(player1(), player2());

            // WHEN
            Game game = new Game(
                    new GameId(1),
                    "game name",
                    eventPublisher,
                    players,
                    new PlayerTurnOrchestrator(null, null, null),
                    players.getCurrent(),
                    CREATED);

            // THEN no domain events are produced
            assertThat(eventPublisher.getEvents()).isEmpty();
            assertThat(game.getAndClearUncommittedEvents()).isEmpty();
        }

        @Test
        void creation_through_factory__should_raise_player_added_event() {
            // GIVEN
            eventPublisher.clearEvents();

            // WHEN
            Player player1 = player1();
            Player player2 = player2();
            Game game = gameFactory.create("game", player1, player2);

            // THEN the domain events are produced in the correct order
            List<Event> events = eventPublisher.getEvents();
            Assertions.assertArrayEquals(events.toArray(), new Event[]{
                    new PlayerAddedEvent(player1, 1),
                    new PlayerAddedEvent(player2, 2),
                    new GameCreatedEvent(game.getId())
            });
        }
    }

    @Nested
    class JoinGame {
        @ParameterizedTest
        @EnumSource(value = State.class, names = {"STARTED", "ENDED"})
        void cannot_join_game__when_state_is_not_CREATED(State state) {
            // GIVEN
            Game game = TestFixtures.a1playerGame();
            game.setState(state);

            // WHEN
            assertThatThrownBy(() -> game.addPlayer(player2()))
                    .isInstanceOf(InvalidGameStateException.class);
        }

        @Test
        void cannot_join_game__when_existing_player_with_same_name() {
            // GIVEN
            Game game = TestFixtures.a1playerGame();

            // WHEN
            assertThatThrownBy(() -> game.addPlayer(player1()))
                    .isInstanceOf(DuplicatePlayerNameException.class)
                    .hasMessageStartingWith("duplicate player name on player join");
        }

        @Test
        void cannot_join_game__when_max_player_count_reached() {
            // GIVEN
            Game game = TestFixtures.a6playersGame();

            // WHEN
            assertThatThrownBy(() -> game.addPlayer(player(7)))
                    .isInstanceOf(InvalidNumberOfPlayersException.class);
        }

        @Test
        void game_id_is_set__when_joining_game() {
            // GIVEN
            Game game = TestFixtures.a1playerGame();
            Player newPlayer = player2();

            // WHEN
            game.addPlayer(newPlayer);

            // THEN
            assertThat(newPlayer.getGameId()).isEqualTo(game.getId());
        }
    }

    @Nested
    class StartGame {

        @Test
        void creator_can_start_game() {
            // WHEN
            game.startBy(player1);

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
                softAssertions.assertThatThrownBy(() -> game.startBy(player2)).isInstanceOf(StartException.class);
                softAssertions.assertThat(game.getState()).isEqualTo(State.CREATED);
            });
        }

        @Test
        void cannot_start_a_game_with_less_than_2_players() {
            // GIVEN a 1 player game
            game = gameFactory.create("game", player1);

            // WHEN
            ThrowableAssert.ThrowingCallable callable = () -> game.startBy(player1);

            // THEN
            assertSoftly(softAssertions -> {
                softAssertions.assertThatThrownBy(callable).isInstanceOf(StartException.class);
                softAssertions.assertThat(game.getState()).isEqualTo(State.CREATED);
            });
        }
    }

    @Nested
    class PlayTurn {
        @Test
        void player_other_than_current_should_not_be_able_to_play_turn() {
            assertThatThrownBy(() -> game.playTurnBy(player2)).isInstanceOf(PlayTurnException.class);
        }

        @Test
        void current_player_should_be_able_to_play_turn() {
            assertSoftly(softAssertions -> {
                softAssertions.assertThatCode(() -> game.playTurnBy(player1)).doesNotThrowAnyException();
                softAssertions.assertThat(game.getCurrentPlayer()).isEqualTo(player2);
            });
        }

    }

    @Nested
    class EndGame {
        @Test
        void game_should_end__if_current_player_is_winning() {
            // GIVEN
            GameId gameId = new GameId(1);
            Players players = Mockito.mock(Players.class);
            PlayerTurnOrchestrator playerTurnOrchestrator = Mockito.mock(PlayerTurnOrchestrator.class);
            Player player = Mockito.spy(player1());
            Mockito.doReturn(true).when(player).isWinning();
            Game game = new Game(gameId, null, eventPublisher, players, playerTurnOrchestrator, player, STARTED);

            // WHEN
            game.playTurnBy(player);

            // THEN
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(game.getState()).isEqualTo(ENDED);
                softAssertions.assertThat(eventPublisher.getEvents()).contains(new GameEndedEvent(gameId, player.getId()));
                softAssertions.assertThat(eventPublisher.getEvents()).contains(new PlayerWonEvent(gameId, player));
            });
        }

        @SneakyThrows
        @Test
        void cannot_perform_a_command_on_an_ended_game() {
            // GIVEN an ended game
            game.setState(ENDED);

            // WHEN
            assertSoftly(softAssertions -> {
                softAssertions.assertThatThrownBy(() -> game.addPlayer(player1())).isInstanceOf(InvalidGameStateException.class);
                softAssertions.assertThatThrownBy(() -> game.startBy(player1)).isInstanceOf(InvalidGameStateException.class);
                softAssertions.assertThatThrownBy(() -> game.playTurnBy(player1)).isInstanceOf(InvalidGameStateException.class);
            });

            // THEN
        }
    }
}
