package com.adaptionsoft.games.uglytrivia.event;

import com.adaptionsoft.games.uglytrivia.Player;

public class QuestionAskedToPlayerEvent extends PlayerEvent {
    public QuestionAskedToPlayerEvent(Player player, String question) {
        super(player, question);
    }
}
