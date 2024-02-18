package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class PlayerEvent extends Event {
    protected final String playerName;
    /**
     * if happening before the game started -> turn == 0
     */
    protected final int turn;

    public PlayerEvent(Player player, String stringValue) {
        super(stringValue);
        this.playerName = player.getName();
        this.turn = player.getTurn();
    }
}
