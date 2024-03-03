package com.adaptionsoft.games.trivia.domain;

import java.util.Map;
import java.util.Queue;

public class TestFixtures {

    public static Questions questions(){
        String pathString = "src/test/resources/questions-test";
        return questions(pathString);
    }

    private static Questions questions(String pathString) {
        QuestionsLoader questionsLoader = new QuestionsLoader();
        Map<Questions.Category, Queue<String>> questionsMap = questionsLoader.loadQuestionsFromDirectory(pathString);
        return new Questions(questionsMap);
    }
}
