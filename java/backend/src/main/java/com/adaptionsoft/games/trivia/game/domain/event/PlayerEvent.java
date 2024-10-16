package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.Player;
import com.adaptionsoft.games.trivia.game.domain.UserId;
import com.adaptionsoft.games.trivia.shared.microarchitecture.Event;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class PlayerEvent extends Event {
    protected final UserId playerId;
    protected final String playerName;

    /**
     * if happening before the game started -> turn == 0
     */
    protected final int turn;

    protected PlayerEvent(Player player, String stringValue, int turn) {
        super(player.getGameId(), stringValue);
        this.playerId = player.getId();
        this.playerName = player.getName();
        this.turn = turn;
    }
}
