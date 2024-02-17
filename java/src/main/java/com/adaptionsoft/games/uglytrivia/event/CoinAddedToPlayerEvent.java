package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class CoinAddedToPlayerEvent extends PlayerEvent {
    public CoinAddedToPlayerEvent(Player player, int turn) {
        super(player, turn, "%s now has %d Gold Coins.".formatted(player.getName(), player.getCoinCount()));
    }
}
