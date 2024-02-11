package com.adaptionsoft.games.uglytrivia;

public class PlayerSentToPenaltyBoxEvent extends PlayerEvent {
    public PlayerSentToPenaltyBoxEvent(Player player, int turn) {
        super(player, turn, "%s was sent to the penalty box".formatted(player.getName()));
    }
}
