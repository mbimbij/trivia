package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;
import com.adaptionsoft.games.trivia.domain.QuestionCategory;

public class PlayerChangedLocationEvent extends PlayerEvent {
    public PlayerChangedLocationEvent(Player player, QuestionCategory questionCategory) {
        super(player, buildMessage(player, questionCategory));
    }

    private static String buildMessage(Player player, QuestionCategory questionCategory) {
        return "%s's new location is %d%nThe category is %s".formatted(player.getName(), player.getLocation(), questionCategory);
    }
}
