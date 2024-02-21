package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.Player;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {
    @Test
    void should_be_on_a_winning_streak_after_3_consecutive_good_answers() {
        // GIVEN

        Player player = new Player("name",
                null,
                null,
                null);
        // THEN
        assertThat(player.isOnACorrectAnswersStreak()).isFalse();
        assertThat(player.getCoinCount()).isZero();

        // WHEN 1st correct answer
        player.answerCorrectly();

        // THEN
        assertThat(player.isOnACorrectAnswersStreak()).isFalse();
        assertThat(player.getCoinCount()).isEqualTo(1);

        // WHEN 2nd correct answer
        player.answerCorrectly();

        // THEN
        assertThat(player.isOnACorrectAnswersStreak()).isFalse();
        assertThat(player.getCoinCount()).isEqualTo(2);

        // WHEN 3rd correct answer
        player.answerCorrectly();

        // THEN
        assertThat(player.isOnACorrectAnswersStreak()).isTrue();
        assertThat(player.getCoinCount()).isEqualTo(3);

        // WHEN 4th correct answer
        player.answerCorrectly();

        // THEN
        assertThat(player.isOnACorrectAnswersStreak()).isTrue();
        assertThat(player.getCoinCount()).isEqualTo(5);
    }

    @Test
    void should_end_winning_streak__when_incorrect_answer__and_no_go_to_jail() {
        // GIVEN a player on a winning streak
        Player player = new Player("name",
                null,
                null,
                null);
        player.setConsecutiveCorrectAnswersCount(4);

        // WHEN incorrect answer
        player.answerIncorrectly();

        // THEN the streak is ended
        assertThat(player.isOnACorrectAnswersStreak()).isFalse();

        // BUT the player did not go to the penalty box
        assertThat(player.isInPenaltyBox()).isFalse();
    }

    @Test
    void should_go_to_penalty_box__when_incorrect_answer__given_no_streak() {
        // GIVEN
        Player player = new Player("name",
                null,
                null,
                null);
        assertThat(player.isOnACorrectAnswersStreak()).isFalse();

        // WHEN
        player.answerIncorrectly();

        // THEN
        assertThat(player.isInPenaltyBox()).isTrue();
    }

    @Test
    void should_win_after_collecting_12_coins() {
        Player player = new Player("name",
                null,
                null,
                null);
        assertThat(player.withCoinCount(6).isWinning()).isFalse();
        assertThat(player.withCoinCount(10).isWinning()).isFalse();
        assertThat(player.withCoinCount(12).isWinning()).isTrue();
        assertThat(player.withCoinCount(20).isWinning()).isTrue();
    }
}