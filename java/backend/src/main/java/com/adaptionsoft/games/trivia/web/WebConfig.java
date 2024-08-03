package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.statemachine.State;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public Module stateDeserializer() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(State.class, new StateDeserializer());
        return module;
    }
}
