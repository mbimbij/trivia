package com.adaptionsoft.games.trivia.domain;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionsRepositoryTxtTest {
    @Test
    void can_load_properties_from_file() {
        QuestionsRepository questionsLoader = new QuestionsRepositoryTxt("src/test/resources/questions-test");
        Questions questions = questionsLoader.getQuestions();
        assertThat(questions.isValid()).isTrue();
    }
}