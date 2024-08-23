package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.Dice;
import com.adaptionsoft.games.trivia.game.domain.Player;

public class PlayerRolledDiceEvent extends PlayerEvent {
    public PlayerRolledDiceEvent(Player player, Dice.Roll roll, int turn) {
        super(player, "They have rolled a %d".formatted(roll.value()), turn);
    }
}
