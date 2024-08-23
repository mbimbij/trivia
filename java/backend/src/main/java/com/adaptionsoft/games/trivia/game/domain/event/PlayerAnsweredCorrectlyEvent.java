package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.Player;

public class PlayerAnsweredCorrectlyEvent extends PlayerEvent {
    public PlayerAnsweredCorrectlyEvent(Player player, int turn) {
        super(player, "Answer was correct!!!!", turn);
    }
}
