package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.Player;

public class CoinAddedToPlayerEvent extends PlayerEvent {
    public CoinAddedToPlayerEvent(Player player, int turn) {
        super(player, "%s now has %d Gold Coins.".formatted(player.getName(), player.getCoinCount()), turn);
    }
}
