package com.adaptionsoft.games.trivia.domain.statemachine;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class StateManagerTest {
    @Test
    void simple_automata_test() {
        // GIVEN
        StateManager stateManager = new StateManager(
                "S0",
                List.of(new Transition("S0", "S0", "A0"),
                        new Transition("S0", "S1", "A1"))
        );

        // THEN
        assertSoftly(sa -> {
            sa.assertThat(stateManager.getCurrentState()).isEqualTo("S0");
            sa.assertThat(stateManager.getAuthorizedActions()).isEqualTo(Set.of("A0", "A1"));
            sa.assertThat(stateManager.isActionAuthorized("A0")).isTrue();
            sa.assertThat(stateManager.isActionAuthorized("A1")).isTrue();
            sa.assertThat(stateManager.isActionAuthorized("A2")).isFalse();
            sa.assertThat(stateManager.getNextState("A0")).isEqualTo("S0");
            sa.assertThat(stateManager.getNextState("A1")).isEqualTo("S1");
        });

        // WHEN
        stateManager.applyAction("A0");

        // THEN
        assertSoftly(sa -> {
            sa.assertThat(stateManager.getCurrentState()).isEqualTo("S0");
            sa.assertThat(stateManager.getAuthorizedActions()).isEqualTo(Set.of("A0", "A1"));
        });

        // WHEN
        stateManager.applyAction("A1");

        // THEN
        assertSoftly(sa -> {
            sa.assertThat(stateManager.getCurrentState()).isEqualTo("S1");
            sa.assertThat(stateManager.getAuthorizedActions()).isEqualTo(emptySet());
            sa.assertThat(stateManager.isActionAuthorized("A0")).isFalse();
            sa.assertThat(stateManager.isActionAuthorized("A1")).isFalse();
            sa.assertThat(stateManager.isActionAuthorized("A2")).isFalse();
        });
    }

    @Test
    void apply_unknown_action_should_throw_exception() {
        // GIVEN
        StateManager stateManager = new StateManager(
                "S0",
                List.of(new Transition("S0", "S0", "A0"),
                        new Transition("S0", "S1", "A1"))
        );

        assertThatThrownBy(() -> stateManager.applyAction("unknown"))
                .isInstanceOf(UnauthorizedAction.class);
    }
}