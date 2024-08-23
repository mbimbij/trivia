package com.adaptionsoft.games.trivia.game.web;

import com.adaptionsoft.games.trivia.game.domain.GameState;
import com.adaptionsoft.games.trivia.game.domain.PlayerState;
import com.adaptionsoft.games.trivia.shared.statemachine.State;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class StateDeserializer extends StdDeserializer<State> {
    public StateDeserializer() {
        super(State.class);
    }

    @Override
    public State deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String text = p.getText();
        try {
            return PlayerState.valueOf(text);
        } catch (IllegalArgumentException e) {
            return GameState.valueOf(text);
        }
    }
}
