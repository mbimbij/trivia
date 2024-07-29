package com.adaptionsoft.games.trivia.domain;

import java.util.Queue;

@FunctionalInterface
public interface QuestionsShuffler {
    Queue<Question> shuffle(Queue<Question> questions);
}
