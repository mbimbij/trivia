package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;

public class PlayerRolledDiceEvent extends PlayerEvent {
    public PlayerRolledDiceEvent(Player player, int roll) {
        super(player, "They have rolled a %d".formatted(roll));
    }
}
