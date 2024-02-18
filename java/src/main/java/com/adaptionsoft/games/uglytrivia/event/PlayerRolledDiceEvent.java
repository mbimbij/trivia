package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class PlayerRolledDiceEvent extends PlayerEvent {
    public PlayerRolledDiceEvent(Player player, int roll) {
        super(player, "They have rolled a %d".formatted(roll));
    }
}
