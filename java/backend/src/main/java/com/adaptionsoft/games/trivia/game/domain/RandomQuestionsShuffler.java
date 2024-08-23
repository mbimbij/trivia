package com.adaptionsoft.games.trivia.game.domain;

import java.util.*;

public class RandomQuestionsShuffler implements QuestionsShuffler {
    @Override
    public Queue<Question> shuffle(Queue<Question> questions) {
        ArrayList<Question> originalQuestionList = new ArrayList<>(questions);
        ArrayList<Question> questionList = new ArrayList<>(questions);
        do {
            Collections.shuffle(questionList);
        }while (Objects.equals(originalQuestionList, questionList));
        return new ArrayDeque<>(questionList);
    }
}
