package com.adaptionsoft.games.trivia.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.adaptionsoft.games.trivia.domain.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = player1();
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
    void should_win_after_collecting_12_coins() {
        assertThat(player.withCoinCount(6).isWinning()).isFalse();
        assertThat(player.withCoinCount(10).isWinning()).isFalse();
        assertThat(player.withCoinCount(12).isWinning()).isTrue();
        assertThat(player.withCoinCount(20).isWinning()).isTrue();
    }

    @Test
    void should_go_to_penalty_box_only_after_2_consecutive_wrong_answers() {
        // WHEN 1st incorrect answer
        player.answerIncorrectly();

        // THEN not in penalty box
        assertThat(player.isInPenaltyBox()).isFalse();

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
        Player player1 = new Player(new UserId("playerId1"), "player1");
        Player player2 = new Player(new UserId("playerId1"), "player2");
        Player player3 = new Player(new UserId("playerId2"), "player2");
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(player1).isEqualTo(player2);
            softAssertions.assertThat(player1).isNotEqualTo(player3);
            softAssertions.assertThat(player2).isNotEqualTo(player3);
        });
    }
}