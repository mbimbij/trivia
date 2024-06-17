package com.adaptionsoft.games.trivia.domain;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Queue;

import static com.adaptionsoft.games.trivia.domain.Questions.Category.*;
import static org.assertj.core.api.Assertions.assertThat;

class QuestionsRepositoryJsonTest {
    @Test
    void can_load_questions_from_file() {
        // GIVEN
        QuestionsRepositoryJson questionsRepository = new QuestionsRepositoryJson("src/test/resources/questions-test-json");

        // WHEN
        Map<Questions.Category, Queue<Question>> questions = questionsRepository.getQuestions();

        // THEN
        assertThat(questions).containsKeys(GEOGRAPHY, POP, ROCK, SCIENCE, SPORTS);
        assertThat(questions).allSatisfy((category, categoryQuestions) -> {
            assertThat(categoryQuestions).hasSize(4);
            assertThat(categoryQuestions).allSatisfy(question -> {
                assertThat(question).hasNoNullFieldsOrProperties();
            });
        });
    }
}