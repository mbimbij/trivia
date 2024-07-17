package com.adaptionsoft.games.trivia.domain.statemachine;

import com.speedment.common.mapstream.MapStream;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

public class StateManager {
    private final Map<StateActionPair, String> nextStateByStateAndAction;
    @Getter
    String currentState;
    private final Map<String, Set<String>> actionsByState;

    public StateManager(String initialState, List<Transition> transitions) {
        this.currentState = initialState;

        actionsByState = transitions.stream()
                .collect(Collectors.groupingBy(
                        Transition::startState,
                        Collectors.mapping(Transition::action, Collectors.toSet())));

        Map<StateActionPair, List<String>> map = transitions.stream()
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

    public Set<String> getAuthorizedActions() {
        return actionsByState.getOrDefault(currentState, emptySet());
    }

    public void applyAction(String action) {
        if(!actionsByState.get(currentState).contains(action)){
            throw new UnauthorizedAction(action);
        }
        this.currentState = getNextState(action);
    }

    public String getNextState(String action) {
        return nextStateByStateAndAction.get(new StateActionPair(currentState, action));
    }

    public boolean isActionAuthorized(String action) {
        return getAuthorizedActions().contains(action);
    }
}