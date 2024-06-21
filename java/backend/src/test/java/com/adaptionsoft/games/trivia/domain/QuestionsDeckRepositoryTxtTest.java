package com.adaptionsoft.games.trivia.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionsDeckRepositoryTxtTest {
    @Test
    void can_load_properties_from_file() {
        QuestionsRepository questionsLoader = new QuestionsRepositoryTxt("src/test/resources/questions-test");
        QuestionsDeck questions = questionsLoader.getQuestions();
        assertThat(questions.isValid()).isTrue();
    }
}