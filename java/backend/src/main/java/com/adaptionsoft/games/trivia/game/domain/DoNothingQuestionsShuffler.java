package com.adaptionsoft.games.trivia.game.domain;

import java.util.Queue;

public class DoNothingQuestionsShuffler implements QuestionsShuffler {
    @Override
    public Queue<Question> shuffle(Queue<Question> questions) {
        return questions;
    }
}
