package com.adaptionsoft.games.trivia.domain;


import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.domain.exception.*;
import com.adaptionsoft.games.trivia.infra.EventConsoleLogger;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.adaptionsoft.games.trivia.domain.AnswerCode.*;
import static com.adaptionsoft.games.trivia.domain.State.*;
import static com.adaptionsoft.games.trivia.domain.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GameTest {

    private final PrintStream console = System.out;
    private final MockEventPublisher eventPublisher = eventPublisher();
    private final GameFactory gameFactory = gameFactory();
    private final PlayerFactory playerFactory = playerFactory();
    private Player player1;
    private Player player2;
    private Game game;

    @BeforeEach
    void setUp() {
        eventPublisher.clearEvents();
        player1 = player1();
        player2 = player2();
        game = gameFactory.create(new Random(2), "game", player1, player2);
    }

    @Test
    void player_other_than_current_should_not_be_able_to_do_anything() {
        game.startBy(player1);
        assertSoftly(softAssertions -> {
            softAssertions.assertThatThrownBy(() -> game.submitAnswerToCurrentQuestion(player2, A)).isInstanceOf(PlayTurnException.class);
            softAssertions.assertThatThrownBy(() -> game.rollDice(player2)).isInstanceOf(PlayTurnException.class);
            softAssertions.assertThatThrownBy(() -> game.drawQuestion(player2)).isInstanceOf(PlayTurnException.class);
        });
    }

    @AfterEach
    void tearDown() {
        System.setOut(console);
    }

    @Nested
    public class GoldenMaster {
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
        void golden_master_test() throws IOException {
            // GIVEN
            redirectStdoutToFile();
            String gold = Files.readString(Paths.get("src/test/resources/gold.txt"));
            int seed = 2;
            Player chet = playerFactory.create(new UserId("id-Chet"), "Chet");
            Player pat = playerFactory.create(new UserId("id-Pat"), "Pat");
            Player sue = playerFactory.create(new UserId("id-Sue"), "Sue");
            Player joe = playerFactory.create(new UserId("id-Joe"), "Joe");
            Player vlad = playerFactory.create(new UserId("id-Vlad"), "Vlad");
            Random rand = new Random(seed);
            Game game = gameFactory.create(rand, "game", chet, pat, sue, joe, vlad);

            // WHEN
            playEntireGame(game, rand);

            // THEN
            String lead = Files.readString(Paths.get("src/test/resources/lead.txt"));
            assertEquals(gold, lead);
        }

        private void playEntireGame(Game game, Random rand) {
            game.startBy(game.getCurrentPlayer());
            do {
                if (game.getCurrentPlayer().canRollDice()) {
                    game.rollDice(game.getCurrentPlayer());
                }
                if (canDrawQuestion(game, game.getCurrentPlayer())) {
                    game.drawQuestion(game.getCurrentPlayer());
                }
                if (canSubmitAnswer(game)) {
                    game.submitAnswerToCurrentQuestion(game.getCurrentPlayer(), getRandomAnswer(rand));
                }
            } while (game.isGameInProgress());
        }

        private boolean canDrawQuestion(Game game, Player currentPlayer) {
            return game.getCurrentRoll() != null && currentPlayer.getConsecutiveIncorrectAnswersCount() == 0;
        }

        private boolean canSubmitAnswer(Game game) {
            return !(game.getCurrentRoll() == null) && !game.getCurrentPlayer().isInPenaltyBox();
        }

        private AnswerCode getRandomAnswer(Random rand) {
            return AnswerCode.values()[rand.nextInt(AnswerCode.values().length)];
        }

        @SneakyThrows
        private void redirectStdoutToFile() {
            System.setOut(new PrintStream("src/test/resources/lead.txt"));
        }
    }

    @Nested
    class CreateGame {

        @Test
        void cannot_create_game_with_more_than_6_players() {
            assertThrows(InvalidNumberOfPlayersException.class, () -> gameFactory.create("game",
                    player1(),
                    player2(),
                    player(3),
                    player(4),
                    player(5),
                    player(6),
                    player(7)));
        }

        @Test
        void can_create_game_with_1_player() {
            assertThatCode(() -> gameFactory.create("game", player1())).doesNotThrowAnyException();
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

                Player creator = playerWithRandomId(creatorName);
                Player[] otherPlayers = Arrays.stream(otherPlayersNames)
                        .map(this::playerWithRandomId)
                        .toArray(Player[]::new);

                assertThrows(DuplicatePlayerNameException.class, () -> gameFactory.create("game", creator, otherPlayers));
            }
        }

        private Player playerWithRandomId(String creatorName) {
            return playerFactory.create(new UserId(RandomStringUtils.random(4)), creatorName);
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

            // WHEN
            Game game = new Game(
                    new GameId(1),
                    "game name",
                    CREATED, eventPublisher,
                    null,
                    null,
                    null,
                    player1,
                    player2);

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
                    new PlayerAddedEvent(player1, 1, player1.getTurn()),
                    new PlayerAddedEvent(player2, 2, player2.getTurn()),
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
    class DrawQuestion {
        @Test
        void cannot_draw_question__before_rolling_dice() {
            // GIVEN a started game
            game.startBy(player1);

            // WHEN
            assertThatThrownBy(() -> game.drawQuestion(player1))
                    .isInstanceOf(CannotDrawQuestionBeforeRollingDiceException.class);
        }
    }

    @Nested
    class AnswerQuestion {
        @Test
        void current_player_should_be_able_to_answer() {
            // GIVEN a started game
            game.startBy(player1);
            game.rollDice(player1);
            game.drawQuestion(player1);

            // WHEN
            ThrowableAssert.ThrowingCallable action = () -> game.submitAnswerToCurrentQuestion(player1, A);

            // THEN
            assertThatCode(action).doesNotThrowAnyException();
        }

        @Test
        void cannot_submit_an_answer_if_game_not_started() {
            assumeThat(game.getState()).isNotEqualTo(STARTED);
            assertThatThrownBy(() -> game.submitAnswerToCurrentQuestion(player1, A))
                    .isInstanceOf(InvalidGameStateException.class);
        }

        @Test
        void test_correct_answer() {
            // GIVEN stdout redirected to a string
            ByteArrayOutputStream baos = redirectStdoutToString();

            // AND the expected output
            String expectedOutput = """
                    Game Id(value=1) started
                    player1 is the current player
                    They have rolled a 5
                    player1's new location is 5
                    The category is Pop
                    pop question 1
                    Answer was correct!!!!
                    player1 now has 1 Gold Coins.
                    player2 is the current player
                    """;

            // AND a started game
            game.startBy(player1);
            game.rollDice(game.getCurrentPlayer());
            int turn = game.getTurn();
            game.drawQuestion(player1);

            // WHEN correct answer
            game.submitAnswerToCurrentQuestion(player1, A);

            // THEN event is raised
            assertThat(eventPublisher.getPublishedEvents()).contains(new PlayerAnsweredCorrectlyEvent(player1, turn));

            // AND turn is incremented
            assertThat(game.getTurn()).isEqualTo(turn + 1);

            // AND current player has been changed
            assertThat(game.getCurrentPlayer()).isEqualTo(player2);

            // AND current question has been set to null
            assertThat(game.getCurrentQuestion()).isNull();

            // AND current roll has been set to null
            assertThat(game.getCurrentRoll()).isNull();

            // AND output as expected
            assertThat(baos.toString()).isEqualTo(expectedOutput);
        }

        @Test
        void test_incorrect_answer() {
            // GIVEN stdout redirected to a string
            ByteArrayOutputStream baos = redirectStdoutToString();

            // AND the expected output
            String expectedOutput = """
                    Game Id(value=1) started
                    player1 is the current player
                    They have rolled a 5
                    player1's new location is 5
                    The category is Pop
                    pop question 1
                    Question was incorrectly answered
                    pop question 2
                    Question was incorrectly answered
                    player1 was sent to the penalty box
                    player2 is the current player
                    """;

            // AND a started game
            game.startBy(player1);
            game.rollDice(player1);
            game.drawQuestion(player1);

            // AND some expected values
            int turnBefore = game.getTurn();
            PlayerAnsweredIncorrectlyEvent expectedEvent = new PlayerAnsweredIncorrectlyEvent(player1, player1.getTurn());
            Question currentQuestionBefore = game.getCurrentQuestion();
            assertThat(currentQuestionBefore).isNotNull();

            // WHEN first incorrect answer (correct answers order: A,B,C,D,A,B,C,D)
            game.submitAnswerToCurrentQuestion(player1, B);
            // THEN event is raised
            assertThat(eventPublisher.getPublishedEvents()).containsOnlyOnce(expectedEvent);
            // AND turn not incremented
            assertThat(game.getTurn()).isEqualTo(turnBefore);
            // AND current player has not been changed
            assertThat(game.getCurrentPlayer()).isEqualTo(player1);
            // BUT the current question has been changed
            assertThat(game.getCurrentQuestion())
                    .isNotNull()
                    .isNotEqualTo(currentQuestionBefore);

            // WHEN second consecutive answer (correct answers order: A,B,C,D,A,B,C,D)
            game.submitAnswerToCurrentQuestion(player1, C);
            // THEN output as expected
            assertThat(baos.toString()).isEqualTo(expectedOutput);
            // AND the current question has been set to null
            assertThat(game.getCurrentQuestion()).isNull();
            // AND current roll has been set to null
            assertThat(game.getCurrentRoll()).isNull();
        }

        @Test
        void given_player_in_penalty_box__when_roll_not_pair__then_stay_in_penalty_box() {
            Dice dice = new LoadedDice(3);
            game.setDice(dice);
            Player currentPlayer = game.getCurrentPlayer();
            currentPlayer.setInPenaltyBox(true);
            ByteArrayOutputStream baos = redirectStdoutToString();
            String expectedOutput = """
                    Game Id(value=1) started
                    player1 is the current player
                    They have rolled a 3
                    player1 is not getting out of the penalty box
                    player2 is the current player
                    """;
            game.startBy(currentPlayer);

            // WHEN
            game.rollDice(currentPlayer);

            // THEN
            assertThat(baos.toString()).isEqualTo(expectedOutput);
        }

        @Test
        void given_player_in_penalty_box__when_roll_pair__then_get_out_in_penalty_box() {
            Dice dice = new LoadedDice(2);
            game.setDice(dice);
            Player currentPlayer = game.getCurrentPlayer();
            currentPlayer.setInPenaltyBox(true);
            ByteArrayOutputStream baos = redirectStdoutToString();
            String expectedOutput = """
                    Game Id(value=1) started
                    player1 is the current player
                    They have rolled a 2
                    player1 is getting out of the penalty box
                    player1's new location is 2
                    The category is Sports
                    """;
            game.startBy(currentPlayer);

            // WHEN
            game.rollDice(currentPlayer);

            // THEN
            assertThat(baos.toString()).isEqualTo(expectedOutput);
        }

        @Test
        void cannot_answer_questions_while_in_penalty_box() {
            game.startBy(player1);
            game.rollDice(player1);
            player1.setInPenaltyBox(true);

            assertThatThrownBy(() -> game.submitAnswerToCurrentQuestion(player1, A))
                    .isInstanceOf(ExecuteActionInPenaltyBoxException.class);
        }

        @Test
        void cannot_answer_question_before_drawing_one() {
            game.startBy(player1);
            game.rollDice(player1);
            player1.setInPenaltyBox(true);

            assertThatThrownBy(() -> game.submitAnswerToCurrentQuestion(player1, A))
                    .isInstanceOf(ExecuteActionInPenaltyBoxException.class);
        }
    }

    private ByteArrayOutputStream redirectStdoutToString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        return baos;
    }

    @Nested
    class EndGame {
        @Test
        void game_should_end__after_answering__if_current_player_is_winning() {
            // GIVEN a started game with current player about to win
            Player currentPlayer = Mockito.spy(player1);
            Mockito.doReturn(true).when(currentPlayer).isWinning();
            Question currentQuestion = mock(Question.class);
            doReturn(true).when(currentQuestion).isCorrect(any());

            Game game = a2playersGame();
            game.setState(STARTED);
            game.setCurrentPlayer(currentPlayer);
            game.setCurrentQuestion(currentQuestion);

            // AND stdout redirected to a string
            ByteArrayOutputStream baos = redirectStdoutToString();
            String expectedOutput = """
                    Answer was correct!!!!
                    player1 now has 1 Gold Coins.
                    player Id(value=id-player1) won game Id(value=1)
                    game Id(value=1) ended with winner player1
                    """;

            // WHEN
            game.submitAnswerToCurrentQuestion(currentPlayer, A);

            // THEN
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(game.getState()).isEqualTo(ENDED);
                softAssertions.assertThat(eventPublisher.getPublishedEvents()).contains(new GameEndedEvent(game.getId(), currentPlayer.getName()));
                softAssertions.assertThat(eventPublisher.getPublishedEvents()).contains(new PlayerWonEvent(game.getId(), currentPlayer, currentPlayer.getTurn()));
                softAssertions.assertThat(baos.toString()).isEqualTo(expectedOutput);
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
                softAssertions.assertThatThrownBy(() -> game.rollDice(player1)).isInstanceOf(InvalidGameStateException.class);
                softAssertions.assertThatThrownBy(() -> game.submitAnswerToCurrentQuestion(player1, A)).isInstanceOf(InvalidGameStateException.class);
            });
        }
    }
}
