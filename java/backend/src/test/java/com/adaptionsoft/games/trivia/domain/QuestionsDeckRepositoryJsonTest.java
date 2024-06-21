package com.adaptionsoft.games.trivia.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionsDeckRepositoryJsonTest {
    @Test
    void can_load_questions_from_file() {
        // GIVEN
        QuestionsRepositoryJson questionsRepository = new QuestionsRepositoryJson("src/test/resources/questions-test-json");

        // WHEN
        QuestionsDeck questions = questionsRepository.getQuestions();

        // THEN
        assertThat(questions.isValid()).isTrue();
    }
}