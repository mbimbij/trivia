package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.Player;

public class PlayerAnsweredIncorrectlyEvent extends PlayerEvent {
    public PlayerAnsweredIncorrectlyEvent(Player player, int turn) {
        super(player, "Question was incorrectly answered", turn);
    }
}
