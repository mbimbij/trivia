package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class PlayerAnsweredCorrectlyEvent extends PlayerEvent {
    public PlayerAnsweredCorrectlyEvent(Player player, int turn) {
        super(player, turn, "Answer was correct!!!!");
    }
}
