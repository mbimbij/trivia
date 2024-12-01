package com.adaptionsoft.games.trivia.game.domain.questions;

import java.util.Queue;

@FunctionalInterface
public interface QuestionsShuffler {
    Queue<Question> shuffle(Queue<Question> questions);
}
