package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.exception.CannotUpdateLocationFromPenaltyBoxException;
import com.adaptionsoft.games.trivia.domain.statemachine.StateManager;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.*;

import static com.adaptionsoft.games.trivia.domain.TestFixtures.player1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;

class PlayerTest {

    private Player player;

    @Nested
    class StateInsensitiveTests{
        @BeforeEach
        void setUp() {
            player = player1();
            player.setStateManager(mock(StateManager.class));
        }

        @Test
        void should_be_on_a_winning_streak_after_3_consecutive_good_answers() {
            // THEN
            assertThat(player.isOnAStreak()).isFalse();
            assertThat(player.getCoinCount()).isZero();

            // WHEN 1st correct answer
            player.answerCorrectly();

            // THEN
            assertThat(player.isOnAStreak()).isFalse();
            assertThat(player.getCoinCount()).isEqualTo(1);

            // WHEN 2nd correct answer
            player.answerCorrectly();

            // THEN
            assertThat(player.isOnAStreak()).isFalse();
            assertThat(player.getCoinCount()).isEqualTo(2);

            // WHEN 3rd correct answer
            player.answerCorrectly();

            // THEN
            assertThat(player.isOnAStreak()).isTrue();
            assertThat(player.getCoinCount()).isEqualTo(3);

            // WHEN 4th correct answer
            player.answerCorrectly();

            // THEN
            assertThat(player.isOnAStreak()).isTrue();
            assertThat(player.getCoinCount()).isEqualTo(5);
        }

        @Test
        void should_end_winning_streak__when_incorrect_answer__and_no_go_to_jail() {
            // GIVEN a player on a winning streak
            player.setConsecutiveCorrectAnswersCount(4);

            // WHEN incorrect answer
            player.answerIncorrectly();

            // THEN the streak is ended
            assertThat(player.isOnAStreak()).isFalse();

            // BUT the player did not go to the penalty box
            assertThat(player.isInPenaltyBox()).isFalse();
        }

        @Test
        void should_win_after_collecting_6_coins() {
            assertThat(player.withCoinCount(2).isWinning()).isFalse();
            assertThat(player.withCoinCount(6).isWinning()).isTrue();
            assertThat(player.withCoinCount(10).isWinning()).isTrue();
            assertThat(player.withCoinCount(12).isWinning()).isTrue();
            assertThat(player.withCoinCount(20).isWinning()).isTrue();
        }

        @Test
        void should_go_to_penalty_box_only_after_2_consecutive_wrong_answers() {
            // WHEN 1st incorrect answer
            player.answerIncorrectly();

            // THEN not in penalty box
            assertThat(player.isInPenaltyBox()).isFalse();

            // WHEN
            player.applyAction(PlayerAction.DRAW_QUESTION);
            player.applyAction(PlayerAction.SUBMIT_ANSWER);

            // WHEN 2nd incorrect answer
            player.answerIncorrectly();

            // THEN not in penalty box
            assertThat(player.isInPenaltyBox()).isTrue();
        }

        @Test
        void should_not_go_to_penalty_box_after_1_wrong_answer_followed_by_correct_answer() {
            // WHEN 1st incorrect answer
            player.answerIncorrectly();

            // THEN not in penalty box
            assertThat(player.isInPenaltyBox()).isFalse();

            // WHEN 2nd incorrect answer
            player.answerCorrectly();

            // THEN not in penalty box
            assertThat(player.isInPenaltyBox()).isFalse();
        }

        @Test
        void equality_on_id_only() {
            Player player1 = new Player(mock(EventPublisher.class), new UserId("playerId1"), "player1");
            Player player2 = new Player(mock(EventPublisher.class), new UserId("playerId1"), "player2");
            Player player3 = new Player(mock(EventPublisher.class), new UserId("playerId2"), "player2");
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(player1).isEqualTo(player2);
                softAssertions.assertThat(player1).isNotEqualTo(player3);
                softAssertions.assertThat(player2).isNotEqualTo(player3);
            });
        }

        @Test
        void getting_out_of_penalty_box_set_inner_state_correctly() {
            // GIVEN
            player.answerIncorrectly();
            player.answerIncorrectly();

            // WHEN
            player.getOutOfPenaltyBox();

            // THEN
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(player.isInPenaltyBox()).isFalse();
                softAssertions.assertThat(player.getConsecutiveCorrectAnswersCount()).isEqualTo(0);
                softAssertions.assertThat(player.getConsecutiveIncorrectAnswersCount()).isEqualTo(0);
            });
        }

        @Test
        void cannot_update_location__if_in_penalty_box() {
            // GIVEN
            player.setInPenaltyBox(true);

            assertThatCode(() -> player.updateLocation(3))
                    .isInstanceOf(CannotUpdateLocationFromPenaltyBoxException.class);
        }

        @Test
        void correct_answer__should_reset_incorrect_answer_counter() {
            // GIVEN
            player.answerIncorrectly();

            // THEN
            assertThat(player.getConsecutiveIncorrectAnswersCount()).isPositive();

            // WHEN
            player.answerCorrectly();

            // THEN
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(player.isInPenaltyBox()).isFalse();
                softAssertions.assertThat(player.getConsecutiveCorrectAnswersCount()).isEqualTo(1);
                softAssertions.assertThat(player.getConsecutiveIncorrectAnswersCount()).isEqualTo(0);
            });
        }
    }

    @Nested
    class StateSensitiveTests {

        @BeforeEach
        void setUp() {
            player = player1();
        }

        @Test
        void given_player_in_penalty_box__when_odd_dice_roll__then_waiting_for_validation() {
            // GIVEN
            player.setInPenaltyBox(true);

            // WHEN
            player.applyDiceRoll(new Dice.Roll(3), 4);

            // THEN
            assertThat(player.getState())
                    .isEqualTo(PlayerState.WAITING_TO_VALIDATE_ODD_DICE_ROLL_FROM_PENALTY_BOX);
        }

    }
}