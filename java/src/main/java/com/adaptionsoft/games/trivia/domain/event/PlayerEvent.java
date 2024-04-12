package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
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
