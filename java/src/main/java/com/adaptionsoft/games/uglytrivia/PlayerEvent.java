package com.adaptionsoft.games.uglytrivia;

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

    public PlayerEvent(Player player, int turn, String stringValue) {
        super(stringValue);
        this.playerName = player.getName();
        this.turn = turn;
    }
}
