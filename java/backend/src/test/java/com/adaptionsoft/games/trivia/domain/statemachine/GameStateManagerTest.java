package com.adaptionsoft.games.trivia.domain.statemachine;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.adaptionsoft.games.trivia.domain.statemachine.GameStateManagerTest.TestActions.*;
import static com.adaptionsoft.games.trivia.domain.statemachine.GameStateManagerTest.TestStates.S0;
import static com.adaptionsoft.games.trivia.domain.statemachine.GameStateManagerTest.TestStates.S1;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class GameStateManagerTest {
    @Test
    void simple_automata_test() {
        // GIVEN
        StateManager stateManager = new StateManager("entityIdentifier",
                S0,
                new Transition(S0, A0, S0),
                new Transition(S0, TestActions.A1, S1)
        );

        // THEN
        assertSoftly(sa -> {
            sa.assertThat(stateManager.getCurrentState()).isEqualTo(S0);
            sa.assertThat(stateManager.getAuthorizedActions()).isEqualTo(Set.of(A0, TestActions.A1));
            sa.assertThat(stateManager.isActionAuthorized(A0)).isTrue();
            sa.assertThat(stateManager.isActionAuthorized(TestActions.A1)).isTrue();
            sa.assertThat(stateManager.isActionAuthorized(A2)).isFalse();
            sa.assertThat(stateManager.getNextState(A0)).isEqualTo(S0);
            sa.assertThat(stateManager.getNextState(TestActions.A1)).isEqualTo(S1);
        });

        // WHEN
        stateManager.applyAction(A0);

        // THEN
        assertSoftly(sa -> {
            sa.assertThat(stateManager.getCurrentState()).isEqualTo(S0);
            sa.assertThat(stateManager.getAuthorizedActions()).isEqualTo(Set.of(A0, TestActions.A1));
        });

        // WHEN
        stateManager.applyAction(TestActions.A1);

        // THEN
        assertSoftly(sa -> {
            sa.assertThat(stateManager.getCurrentState()).isEqualTo(S1);
            sa.assertThat(stateManager.getAuthorizedActions()).isEqualTo(emptySet());
            sa.assertThat(stateManager.isActionAuthorized(A0)).isFalse();
            sa.assertThat(stateManager.isActionAuthorized(TestActions.A1)).isFalse();
            sa.assertThat(stateManager.isActionAuthorized(A2)).isFalse();
        });
    }

    @Test
    void apply_unknown_action_should_throw_exception() {
        // GIVEN
        StateManager stateManager = new StateManager("entityIdentifier",
                S0,
                new Transition(S0, A0, S0),
                new Transition(S0, TestActions.A1, S1)
        );

        assertThatThrownBy(() -> stateManager.applyAction(UNKNOWN))
                .isInstanceOf(CannotExecuteAction.class);
    }

    enum TestStates implements State {
        S0, S1
    }

    enum TestActions implements Action {
        A0, A1, A2, UNKNOWN
    }
}