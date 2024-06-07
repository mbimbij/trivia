package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;

public class PlayerAnsweredIncorrectlyEvent extends PlayerEvent {
    public PlayerAnsweredIncorrectlyEvent(Player player) {
        super(player, "Question was incorrectly answered");
    }
}
