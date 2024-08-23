package com.adaptionsoft.games.trivia.game.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlayersTest {
    @Test
    void default_shuffler_does_shuffle() {
        // GIVEN
        Player player1 = TestFixtures.player1();
        Player player2 = TestFixtures.player2();
        Player player3 = TestFixtures.player(3);
        Player player4 = TestFixtures.player(4);
        Players players = new Players(TestFixtures.eventPublisher(),
                player1,
                player2,
                player3,
                player4
        );

        // WHEN
        players.shuffle();

        // THEN
        List<Player> expected = Arrays.asList(player1, player2, player3, player4);
        assertThat(players.getIndividualPlayers()).isNotEqualTo(expected);
    }

    @Test
    void do_nothing_shuffler_does_nothing() {
        // GIVEN
        Player player1 = TestFixtures.player1();
        Player player2 = TestFixtures.player2();
        Player player3 = TestFixtures.player(3);
        Player player4 = TestFixtures.player(4);
        Players players = new Players(TestFixtures.eventPublisher(),
                player1,
                player2,
                player3,
                player4
        );
        players.setShuffler(new DoNothingPlayersShuffler());

        // WHEN
        players.shuffle();

        // THEN
        List<Player> expected = Arrays.asList(player1, player2, player3, player4);
        assertThat(players.getIndividualPlayers()).isEqualTo(expected);
    }
}