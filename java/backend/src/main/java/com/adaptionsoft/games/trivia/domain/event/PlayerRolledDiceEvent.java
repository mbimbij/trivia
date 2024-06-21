package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Dice;
import com.adaptionsoft.games.trivia.domain.Player;

public class PlayerRolledDiceEvent extends PlayerEvent {
    public PlayerRolledDiceEvent(Player player, Dice.Roll roll, int turn) {
        super(player, "They have rolled a %d".formatted(roll.value()), turn);
    }
}
