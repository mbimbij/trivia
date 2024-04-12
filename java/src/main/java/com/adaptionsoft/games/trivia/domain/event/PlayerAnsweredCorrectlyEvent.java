package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;

public class PlayerAnsweredCorrectlyEvent extends PlayerEvent {
    public PlayerAnsweredCorrectlyEvent(Player player) {
        super(player, "Answer was correct!!!!");
    }
}
