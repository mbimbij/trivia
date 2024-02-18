package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.*;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;

import static com.adaptionsoft.games.uglytrivia.QuestionInitializationStrategyFactory.Types.PROPERTIES_FILES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PlayerTest {
    @Test
    void should_be_on_a_winning_streak_after_3_consecutive_good_answers() {
        // GIVEN
        Player player = new Player("player name",
                new RandomAnsweringStrategy(new Random()),
                new MockEventPublisher(),
                new Board());

        // THEN
        assertThat(player.isOnAWinningStreak()).isFalse();
        assertThat(player.getCoinCount()).isEqualTo(0);

        // WHEN 1st correct answer
        player.answerCorrectly();

        // THEN
        assertThat(player.isOnAWinningStreak()).isFalse();
        assertThat(player.getCoinCount()).isEqualTo(1);

        // WHEN 2nd correct answer
        player.answerCorrectly();

        // THEN
        assertThat(player.isOnAWinningStreak()).isFalse();
        assertThat(player.getCoinCount()).isEqualTo(2);

        // WHEN 3rd correct answer
        player.answerCorrectly();

        // THEN
        assertThat(player.isOnAWinningStreak()).isTrue();
        assertThat(player.getCoinCount()).isEqualTo(3);
    }

    @Test
    void should_end_winning_streak__when_incorrect_answer__and_not_go_to_jail() {
        // GIVEN a player on a winning streak
        Player player = new Player("player name",
                new RandomAnsweringStrategy(new Random()),
                new MockEventPublisher(),
                new Board());
        player.setConsecutiveCorrectAnswersCount(4);

        // THEN
        assertThat(player.isOnAWinningStreak()).isTrue();

        // WHEN incorrect answer
        player.answerIncorrectly();

        // THEN the streak is ended
        assertThat(player.isOnAWinningStreak()).isFalse();

        // AND the player did not go to the penalty box
        assertThat(player.isInPenaltyBox()).isFalse();
    }

    @Test
    void should_go_to_jail__after_2_wrong_consecutive_answers() {
        // GIVEN
        Random rand = new Random();
        String playerName1 = "player1";
        String playerName2 = "player2";
        FixedAnsweringStrategy player1AnsweringStrategy = new FixedAnsweringStrategy(false);
        MockEventPublisher mockEventPublisher = new MockEventPublisher();
        Board board = new Board();
        Player player1 = new Player(playerName1, player1AnsweringStrategy, mockEventPublisher, board);
        Player player2 = new Player(playerName2, new RandomAnsweringStrategy(rand), mockEventPublisher, board);
        mockEventPublisher.register(new EventConsoleLogger());
        Players players = new Players(mockEventPublisher, player1, player2);
        Game game = GameFactory.create(rand, board, players, new DummyQuestionInitializationStrategy(), mockEventPublisher);
        assertEquals(player1, game.getCurrentPlayer());

        // WHEN
        game.currentPlayer.askQuestion();

        // THEN the domain events are produced in the correct order
        List<Event> events = mockEventPublisher.getEvents();
        assertThat(events)
                .usingRecursiveComparison()
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsSubsequence(
                        new PlayerAnsweredIncorrectlyEvent(player1, 1),
                        new PlayerAnsweredIncorrectlyEvent(player1, 1),
                        new PlayerSentToPenaltyBoxEvent(player1, 1)
                );
    }

    @Test
    void should_go_to_jail__after_2_wrong_consecutive_answers_alt() {
        // GIVEN
        QuestionInitializationStrategyFactory.getInstance(PROPERTIES_FILES).run();
        AnsweringStrategy answeringStrategy = Mockito.mock(AnsweringStrategy.class);
        when(answeringStrategy.isAnsweringCorrectly()).thenReturn(false).thenReturn(false);
        Player player = new Player("player name",
                answeringStrategy,
                new MockEventPublisher(),
                new Board());

        // WHEN
        player.askQuestion();

        // THEN
        assertThat(player.isInPenaltyBox()).isTrue();
    }

    @Test
    void should_go_to_jail__after_1_wrong_answer__and_1_correct_answer() {
        // GIVEN
        QuestionInitializationStrategyFactory.getInstance(PROPERTIES_FILES).run();
        AnsweringStrategy answeringStrategy = Mockito.mock(AnsweringStrategy.class);
        when(answeringStrategy.isAnsweringCorrectly()).thenReturn(false).thenReturn(true);
        Player player = new Player("player name",
                answeringStrategy,
                new MockEventPublisher(),
                new Board());

        // WHEN
        player.askQuestion();

        // THEN
        assertThat(player.isInPenaltyBox()).isFalse();
    }

    @Test
    void should_win_after_collecting_12_coins() {
        Player player = new Player("player name",
                new RandomAnsweringStrategy(new Random()),
                new MockEventPublisher(),
                new Board());
        assertThat(player.withCoinCount(6).isWinning()).isFalse();
        assertThat(player.withCoinCount(10).isWinning()).isFalse();
        assertThat(player.withCoinCount(12).isWinning()).isTrue();
        assertThat(player.withCoinCount(20).isWinning()).isTrue();
    }
}