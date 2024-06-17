package com.adaptionsoft.games.trivia.domain;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionsRepositoryTxtTest {
    @Test
    void can_load_properties_from_file() {
        QuestionsRepository questionsLoader = new QuestionsRepositoryTxt("src/test/resources/questions-test");
        Map<Questions.Category, Queue<Question>> questions = questionsLoader.getQuestions();
        assertThat(questions).isNotEmpty();
        assertThat(questions.keySet()).containsExactlyInAnyOrder(Questions.Category.values());
        assertThat(questions.values()).map(Queue::size).allSatisfy(size -> assertThat(size).isEqualTo(5));
    }
}