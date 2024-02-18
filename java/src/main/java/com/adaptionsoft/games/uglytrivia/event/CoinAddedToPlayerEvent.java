package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class CoinAddedToPlayerEvent extends PlayerEvent {
    public CoinAddedToPlayerEvent(Player player) {
        super(player, "%s now has %d Gold Coins.".formatted(player.getName(), player.getCoinCount()));
    }
}
