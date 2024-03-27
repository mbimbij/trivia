package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.domain.GameRepository;
import com.adaptionsoft.games.trivia.persistence.GameRepositoryInMemory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TriviaApplication {
    public static void main(String[] args) {
        SpringApplication.run(TriviaApplication.class, args);
    }

    @Bean
    public GameRepository gameRepository() {
        return new GameRepositoryInMemory();
    }
}
