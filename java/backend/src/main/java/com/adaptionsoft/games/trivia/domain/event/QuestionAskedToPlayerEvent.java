package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;

public class QuestionAskedToPlayerEvent extends PlayerEvent {
    /**
     * TO be used in tests, to specify the expected turn the event was raised at
     * @param player
     * @param question
     * @param turn
     */
    public QuestionAskedToPlayerEvent(Player player, String question, int turn) {
        super(player, question, turn);
    }
    public QuestionAskedToPlayerEvent(Player player, String question) {
        super(player, question, player.getTurn());
    }
}
