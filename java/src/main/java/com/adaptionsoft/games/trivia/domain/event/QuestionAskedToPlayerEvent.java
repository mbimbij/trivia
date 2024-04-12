package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.Player;

public class QuestionAskedToPlayerEvent extends PlayerEvent {
    public QuestionAskedToPlayerEvent(Player player, String question) {
        super(player, question);
    }
}
