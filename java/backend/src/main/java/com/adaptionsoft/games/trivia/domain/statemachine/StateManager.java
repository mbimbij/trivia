package com.adaptionsoft.games.trivia.domain.statemachine;

import com.adaptionsoft.games.trivia.domain.GameState;
import com.speedment.common.mapstream.MapStream;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

public class StateManager {
    @Getter
    private final String entityIdentifier;
    @Getter
    @Setter //for testing only
    State currentState;
    private final Map<StateActionPair, State> nextStateByStateAndAction;
    private final Map<State, Set<Action>> actionsByState;

    public StateManager(String entityIdentifier, State initialState, Transition... transitions) {
        this.entityIdentifier = entityIdentifier;
        this.currentState = initialState;

        actionsByState = Arrays.stream(transitions)
                .collect(Collectors.groupingBy(
                        Transition::startState,
                        Collectors.mapping(Transition::action, Collectors.toSet())));


        Map<StateActionPair, List<State>> map = Arrays.stream(transitions)
                .collect(Collectors.groupingBy(
                        t -> new StateActionPair(t.startState(), t.action()),
                        Collectors.mapping(Transition::endState, Collectors.toList())));
        map.forEach((pair, nextStates) -> {
            assert nextStates.size() == 1;
        });

        nextStateByStateAndAction = MapStream.of(map)
                .mapValue(List::getFirst)
                .toMap();
    }

    public Set<Action> getAuthorizedActions() {
        return actionsByState.getOrDefault(currentState, emptySet());
    }

    public void applyAction(Action action) {
        validateAction(action);
        this.currentState = getNextState(action);
    }

    public void validateAction(Action action) {
        if (!actionsByState.getOrDefault(currentState, emptySet()).contains(action)) {
            throw new CannotExecuteAction(entityIdentifier, action, currentState);
        }
    }

    public State getNextState(Action action) {
        return nextStateByStateAndAction.get(new StateActionPair(currentState, action));
    }

    public boolean isActionAuthorized(Action action) {
        return getAuthorizedActions().contains(action);
    }

    public void validateState(GameState gameState) {
        assert currentState == gameState;
    }
}