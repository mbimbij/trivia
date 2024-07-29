package com.adaptionsoft.games.trivia.domain;

import java.util.*;

public class RandomQuestionsShuffler implements QuestionsShuffler {
    @Override
    public Queue<Question> shuffle(Queue<Question> questions) {
        ArrayList<Question> questionList = new ArrayList<>(questions);
        Collections.shuffle(questionList);
        return new ArrayDeque<>(questionList);
    }
}
