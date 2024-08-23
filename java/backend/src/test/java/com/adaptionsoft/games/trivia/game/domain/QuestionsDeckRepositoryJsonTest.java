package com.adaptionsoft.games.trivia.game.domain;

import com.adaptionsoft.games.trivia.game.domain.QuestionsDeck;
import com.adaptionsoft.games.trivia.game.domain.QuestionsRepositoryJson;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionsDeckRepositoryJsonTest {
    @Test
    void can_load_questions_from_file() {
        // GIVEN
        QuestionsRepositoryJson questionsRepository = new QuestionsRepositoryJson("src/test/resources/questions");

        // WHEN
        QuestionsDeck questions = questionsRepository.getQuestions();

        // THEN
        assertThat(questions.isValid()).isTrue();
    }
}