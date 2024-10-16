package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.Player;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class PlayerAddedEvent extends PlayerEvent {
    public PlayerAddedEvent(Player player, int playersCount) {
        super(player, buildStringValue(player, playersCount), player.getTurn());
    }

    private static String buildStringValue(Player player, int playersCount) {
        return "%s was added%nThey are player number %d".formatted(player.getName(), playersCount);
    }
}
