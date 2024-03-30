package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.MockEventPublisher;
import com.adaptionsoft.games.trivia.infra.EventConsoleLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class PlayerTurnOrchestratorTest {

    private MockEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        eventPublisher = new MockEventPublisher();
        eventPublisher.register(new EventConsoleLogger());
        eventPublisher.clearEvents();
    }

    @Test
    void should_ask_only_1_question__when_correct_answer() {
        // GIVEN
        String playerName = "player name";
        Player player = new Player(playerName);
        Questions questions = Mockito.spy(TestFixtures.questions());
        PlayerTurnOrchestrator systemUnderTest = spy(
                new PlayerTurnOrchestrator(questions,
                        new Random(),
                        new Board(12))
        );
        doReturn(true).when(systemUnderTest).isAnsweringCorrectly();

        // WHEN
        systemUnderTest.performTurn(player);

        // THEN
        verify(questions).drawQuestion(anyInt());
    }

    @Test
    void should_ask_2_questions__when_incorrect_answers() {
        // GIVEN
        String playerName = "player name";
        Player player = new Player(playerName);
        Questions questions = Mockito.spy(TestFixtures.questions());
        PlayerTurnOrchestrator systemUnderTest = spy(
                new PlayerTurnOrchestrator(questions,
                        new Random(),
                        new Board(12))
        );
        doReturn(false).when(systemUnderTest).isAnsweringCorrectly();

        // WHEN
        systemUnderTest.performTurn(player);

        // THEN
        verify(questions, times(2)).drawQuestion(anyInt());
    }
}