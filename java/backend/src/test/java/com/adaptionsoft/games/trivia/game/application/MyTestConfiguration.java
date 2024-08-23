package com.adaptionsoft.games.trivia.game.application;

import com.adaptionsoft.games.trivia.game.domain.event.MockEventPublisher;
import com.adaptionsoft.games.trivia.game.infra.EventConsoleLogger;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventListener;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Collection;

@TestConfiguration
public class MyTestConfiguration {

    @Bean
    public EventConsoleLogger eventConsoleLogger() {
        return new EventConsoleLogger();
    }

    @Bean
    @Primary
    public MockEventPublisher mockEventPublisher(Collection<EventListener> listeners) {
        MockEventPublisher eventPublisher = new MockEventPublisher();
        listeners.forEach(eventPublisher::register);
        return eventPublisher;
    }
}
