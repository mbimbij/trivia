package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class PlayerAnsweredIncorrectlyEvent extends PlayerEvent {
    public PlayerAnsweredIncorrectlyEvent(Player player, int turn) {
        super(player, turn, "Question was incorrectly answered");
    }
}
