package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.game.domain.*;
import com.adaptionsoft.games.trivia.game.domain.event.ObserverBasedEventPublisher;
import com.adaptionsoft.games.trivia.game.infra.GameRepositoryInMemory;
import com.adaptionsoft.games.trivia.gamelogs.GameLogsPersister;
import com.adaptionsoft.games.trivia.gamelogs.GameLogsRepository;
import com.adaptionsoft.games.trivia.gamelogs.GameLogsRepositoryInMemory;
import com.adaptionsoft.games.trivia.gamelogs.GameLogsWebSocketNotifier;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventListener;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventPublisher;
import com.adaptionsoft.games.trivia.shared.microarchitecture.IdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@SpringBootApplication
public class TriviaApplication {
    public static void main(String[] args) {
        SpringApplication.run(TriviaApplication.class, args);
    }

    @Bean
    public GameRepository gameRepository() {
        return new GameRepositoryInMemory();
    }

    @Bean
    public IdGenerator idGenerator() {
        return new IdGenerator();
    }

    @Bean
    public GameLogsRepository gameLogsRepository() {
        return new GameLogsRepositoryInMemory();
    }

    @Bean
    public PlayerFactory playerFactory(EventPublisher eventPublisher) {
        return new PlayerFactory(eventPublisher);
    }

    @Bean
    public GameFactory gameFactory(IdGenerator idGenerator,
                                   EventPublisher eventPublisher,
                                   QuestionsRepository questionsLoader) {
        return new GameFactory(idGenerator, eventPublisher, questionsLoader);
    }

    @Bean
    public QuestionsRepository questionsLoader(@Value("${application.questions-path}") String questionsPath) {
        return new QuestionsRepositoryJson(questionsPath);
    }

    @Bean
    public GameLogsPersister gameLogsListener(GameLogsRepository gameLogsRepository) {
        return new GameLogsPersister(gameLogsRepository);
    }

    @Bean
    public GameLogsWebSocketNotifier gameLogsWebSocketNotifier(SimpMessagingTemplate messagingTemplate) {
        return new GameLogsWebSocketNotifier(messagingTemplate);
    }

    @Bean
    public EventPublisher eventPublisher(
            Collection<EventListener> listeners) {
        ObserverBasedEventPublisher eventPublisher = new ObserverBasedEventPublisher();
        listeners.forEach(eventPublisher::register);
        return eventPublisher;
    }

//    @Bean
////    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
//    @Scope(value = SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public EventPublisher gameEventPublisher(
//            Collection<EventListener> listeners) {
//        ObserverBasedEventPublisher eventPublisher = new ObserverBasedEventPublisher();
//        listeners.forEach(eventPublisher::register);
//        return eventPublisher;
//    }
}
