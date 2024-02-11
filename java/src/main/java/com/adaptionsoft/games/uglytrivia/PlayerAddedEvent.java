package com.adaptionsoft.games.uglytrivia;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class PlayerAddedEvent extends PlayerEvent {
    public PlayerAddedEvent(Player player) {
        super(player, 0, "%s was added".formatted(player.getName()));
    }
}
