package com.adaptionsoft.games.trivia.domain;


import com.adaptionsoft.games.trivia.domain.Game.State;
import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.domain.exception.*;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import lombok.SneakyThrows;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import java.io.PrintStream;
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

class GameTest {

    private static final PrintStream stdout = System.out;
    private final MockEventPublisher eventPublisher = getEventPublisher();
    private GameFactory gameFactory;
    private final Player player1 = player1();
    private final Player player2 = player2();
    private Game game;

    @BeforeEach
    void setUp() {
        System.setOut(stdout);
        IdGenerator idGenerator = new IdGenerator();
        eventPublisher.clearEvents();
        gameFactory = new GameFactory(idGenerator, eventPublisher, new QuestionsRepositoryTxt("src/main/resources/questions"));
        game = gameFactory.create("game", player1, player2);
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
            Players players = new Players(eventPublisher, player1(), player2());

            // WHEN
            Game game = new Game(
                    new GameId(1),
                    "game name",
                    eventPublisher,
                    players,
                    new PlayerTurnOrchestrator(eventPublisher, null, null, null),
                    players.getCurrent(),
                    CREATED,
                    questions());

            // THEN no domain events are produced
            assertThat(eventPublisher.getPublishedEvents()).isEmpty();
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
            List<Event> events = eventPublisher.getPublishedEvents();
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
                    .isInstanceOf(PlayerAlreadyJoinedException.class);
        }

        @Test
        void cannot_join_game__after_renaming_user() {
            // GIVEN
            Game game = TestFixtures.a1playerGame();
            Player renamedPlayer = game.getPlayersList().stream().findFirst().get();
            renamedPlayer.setName("new name");

            // WHEN
            assertThatThrownBy(() -> game.addPlayer(renamedPlayer))
                    .isInstanceOf(PlayerAlreadyJoinedException.class);
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
                softAssertions.assertThat(eventPublisher.getPublishedEvents())
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
    class AnswerQuestion {
        @Test
        void current_player_should_be_able_to_answer() {
            // GIVEN a started game
            game.startBy(player1);

            // WHEN
            ThrowableAssert.ThrowingCallable action = () -> game.submitAnswerToCurrentQuestion(player1, A);

            // THEN
            assertThatCode(action).doesNotThrowAnyException();
        }

        @Test
        void player_other_than_current_should_not_be_able_to_play_turn() {
            game.startBy(player1);
            assertThatThrownBy(() -> game.submitAnswerToCurrentQuestion(player2, A))
                    .isInstanceOf(PlayTurnException.class);
        }

        @Test
        void cannot_submit_an_answer_if_game_not_started() {
            assumeThat(game.getState()).isNotEqualTo(STARTED);
            assertThatThrownBy(() -> game.submitAnswerToCurrentQuestion(player1, A))
                    .isInstanceOf(InvalidGameStateException.class);
        }

        @Test
        void correct_answer__should_raise_appropriate_event() {
            // GIVEN a started game
            game.startBy(player1);
            int turn = game.getTurn();
            PlayerAnsweredCorrectlyEvent expectedEvent = new PlayerAnsweredCorrectlyEvent(player1);

            // WHEN correct answer
            game.submitAnswerToCurrentQuestion(player1, A);

            // THEN event is raised
            assertThat(eventPublisher.getPublishedEvents()).contains(expectedEvent);

            // AND turn is incremented
            assertThat(game.getTurn()).isEqualTo(turn + 1);

            // AND current player has been changed
            assertThat(game.getCurrentPlayer()).isEqualTo(player2);
        }

        @Test
        void test_incorrect_answer() {
            // GIVEN a started game
            game.startBy(player1);

            // AND turn counter at the beginning
            int turn = game.getTurn();
            PlayerAnsweredIncorrectlyEvent expectedEvent = new PlayerAnsweredIncorrectlyEvent(player1);

            // WHEN first incorrect answer
            game.submitAnswerToCurrentQuestion(player1, B);

            // THEN event is raised
            assertThat(eventPublisher.getPublishedEvents()).contains(expectedEvent);

            // AND turn not incremented
            assertThat(game.getTurn()).isEqualTo(turn);

            // AND current player has not been changed
            assertThat(game.getCurrentPlayer()).isEqualTo(player1);
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
            Game game = new Game(gameId,
                    null,
                    eventPublisher,
                    players,
                    playerTurnOrchestrator,
                    player,
                    STARTED,
                    questions());

            // WHEN
            game.playTurnBy(player);

            // THEN
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(game.getState()).isEqualTo(ENDED);
                softAssertions.assertThat(eventPublisher.getPublishedEvents()).contains(new GameEndedEvent(gameId, player.getId()));
                softAssertions.assertThat(eventPublisher.getPublishedEvents()).contains(new PlayerWonEvent(gameId, player));
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
        }
    }
}
