package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;
import com.adaptionsoft.games.uglytrivia.QuestionCategory;

public class PlayerChangedLocationEvent extends PlayerEvent {
    public PlayerChangedLocationEvent(Player player, int turn, QuestionCategory questionCategory) {
        super(player, turn, buildMessage(player, questionCategory));
    }

    private static String buildMessage(Player player, QuestionCategory questionCategory) {
        return "%s's new location is %d%nThe category is %s".formatted(player.getName(), player.getLocation(), questionCategory);
    }
}
