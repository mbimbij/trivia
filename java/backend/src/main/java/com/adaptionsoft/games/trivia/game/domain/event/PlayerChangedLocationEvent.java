package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.Player;
import com.adaptionsoft.games.trivia.game.domain.QuestionsDeck;

public class PlayerChangedLocationEvent extends PlayerEvent {
    public PlayerChangedLocationEvent(Player player, QuestionsDeck.Category questionCategory, int turn) {
        super(player, buildMessage(player, questionCategory), turn);
    }

    private static String buildMessage(Player player, QuestionsDeck.Category questionCategory) {
        return "%s's new location is %d%nThe category is %s".formatted(player.getName(), player.getLocation(), questionCategory);
    }
}
