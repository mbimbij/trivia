package com.adaptionsoft.games.trivia.domain;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.adaptionsoft.games.trivia.domain.QuestionsDeck.Category.POP;
import static com.adaptionsoft.games.trivia.domain.QuestionsDeck.Category.SCIENCE;
import static org.assertj.core.api.Assertions.assertThat;

class QuestionsDeckTest {
    @Test
    void can_shuffle_deck() {
        // GIVEN a deck with POP and SCIENCE questions
        ArrayDeque<Question> questionsPop = questions();
        ArrayDeque<Question> questionsScience = questions();

        Map<QuestionsDeck.Category, Queue<Question>> questionsByCategory = Map.of(
                POP, questionsPop,
                SCIENCE, questionsScience
        );

        QuestionsDeck questionsDeck = new QuestionsDeck(questionsByCategory);

        // WHEN
        questionsDeck.shuffle();

        // THEN POP questions are shuffled
        Queue<Question> actual = questionsDeck.getQuestionsByCategory().get(POP);
        List<String> expectedQuestionIds = questionsPop.stream().map(Question::id).toList();
        List<String> actualQuestionIds = actual.stream().map(Question::id).toList();
        assertThat(actualQuestionIds).isNotEqualTo(expectedQuestionIds);

        // THEN SCIENCE questions are shuffled
        Queue<Question> actualScience = questionsDeck.getQuestionsByCategory().get(SCIENCE);
        List<String> expectedScienceQuestionIds = questionsScience.stream().map(Question::id).toList();
        List<String> actualPopQuestionIds = actualScience.stream().map(Question::id).toList();
        assertThat(actualPopQuestionIds).isNotEqualTo(expectedScienceQuestionIds);
    }

    @Test
    void can_leave_deck_as_is() {
        // GIVEN a deck with POP and SCIENCE questions
        ArrayDeque<Question> questionsPop = questions();
        ArrayDeque<Question> questionsScience = questions();

        Map<QuestionsDeck.Category, Queue<Question>> questionsByCategory = Map.of(
                POP, questionsPop,
                SCIENCE, questionsScience
        );

        QuestionsDeck questionsDeck = new QuestionsDeck(questionsByCategory);
        questionsDeck.setShuffler(new DoNothingQuestionsShuffler());

        // WHEN
        questionsDeck.shuffle();

        // THEN POP questions are shuffled
        Queue<Question> actual = questionsDeck.getQuestionsByCategory().get(POP);
        List<String> expectedQuestionIds = questionsPop.stream().map(Question::id).toList();
        List<String> actualQuestionIds = actual.stream().map(Question::id).toList();
        assertThat(actualQuestionIds).isEqualTo(expectedQuestionIds);

        // THEN SCIENCE questions are shuffled
        Queue<Question> actualScience = questionsDeck.getQuestionsByCategory().get(SCIENCE);
        List<String> expectedScienceQuestionIds = questionsScience.stream().map(Question::id).toList();
        List<String> actualPopQuestionIds = actualScience.stream().map(Question::id).toList();
        assertThat(actualPopQuestionIds).isEqualTo(expectedScienceQuestionIds);
    }

    private static ArrayDeque<Question> questions() {
        Question questionPop0 = TestFixtures.questionTest(0);
        Question questionPop1 = TestFixtures.questionTest(1);
        Question questionPop2 = TestFixtures.questionTest(2);
        Question questionPop3 = TestFixtures.questionTest(3);
        return new ArrayDeque<>(Arrays.asList(questionPop0, questionPop1, questionPop2, questionPop3));
    }
}