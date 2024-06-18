package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;

public class CoinAddedToPlayerEvent extends PlayerEvent {
    public CoinAddedToPlayerEvent(Player player, int turn) {
        super(player, "%s now has %d Gold Coins.".formatted(player.getName(), player.getCoinCount()), turn);
    }
}
