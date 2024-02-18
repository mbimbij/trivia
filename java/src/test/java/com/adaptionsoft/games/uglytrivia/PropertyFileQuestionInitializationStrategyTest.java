package com.adaptionsoft.games.uglytrivia;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PropertyFileQuestionInitializationStrategyTest {
    @Test
    void can_load_properties_from_file() {
        PropertyFileQuestionInitializationStrategy initializationStrategy = new PropertyFileQuestionInitializationStrategy();
        Map<QuestionCategory, List<String>> questions = initializationStrategy.loadQuestionsFromDirectory("src/test/resources/questions-test");
        assertThat(questions).isNotEmpty();
        assertThat(questions.keySet()).containsExactlyInAnyOrder(QuestionCategory.values());
        assertThat(questions.values()).map(List::size).allSatisfy(size -> assertThat(size).isEqualTo(5));
    }
}