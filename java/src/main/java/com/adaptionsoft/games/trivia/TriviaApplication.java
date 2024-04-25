package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameFactory;
import com.adaptionsoft.games.trivia.domain.GameRepository;
import com.adaptionsoft.games.trivia.domain.QuestionsLoader;
import com.adaptionsoft.games.trivia.domain.event.ObserverBasedEventPublisher;
import com.adaptionsoft.games.trivia.infra.GameRepositoryInMemory;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TriviaApplication {
    public static void main(String[] args) {
        SpringApplication.run(TriviaApplication.class, args);
    }

    @Bean
    public GameRepository gameRepository(IdGenerator idGenerator) {
        return new GameRepositoryInMemory(idGenerator);
    }

    @Bean
    public IdGenerator idGenerator() {
        return new IdGenerator();
    }

    @Bean
    public GameFactory gameFactory() {
        return new GameFactory(new ObserverBasedEventPublisher(), new QuestionsLoader());
    }
}
