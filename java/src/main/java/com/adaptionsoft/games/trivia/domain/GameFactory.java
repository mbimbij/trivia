package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.infra.EventConsoleLogger;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import com.adaptionsoft.games.trivia.domain.event.GameCreatedEvent;

import java.util.*;


public class GameFactory {

    private final EventPublisher eventPublisher;
    private final QuestionsLoader questionsLoader = new QuestionsLoader();

    public GameFactory(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public Game create(String... playersNames) {
        return create(new Random(), playersNames);
    }

    public Game create(Random rand, String... playersNames) {
        Questions questions = buildQuestions();
        int squaresCount = 12;
        Player[] playersArray = Arrays.stream(playersNames)
                .map(playersName -> new Player(playersName,
                        questions,
                        rand,
                        squaresCount))
                .toArray(Player[]::new);
        eventPublisher.register(new EventConsoleLogger());
        Players players = new Players(playersArray);
        eventPublisher.raise(players.getAndClearUncommittedEvents());

        Game game = new Game(
                players,
                eventPublisher
        );

        eventPublisher.raise(new GameCreatedEvent());
        return game;
    }

    private Questions buildQuestions() {
        String questionsPath = "src/main/resources/questions";
        Map<Questions.Category, Queue<String>> questionsByCategory = questionsLoader.loadQuestionsFromDirectory(questionsPath);
        return new Questions(questionsByCategory);
    }
}
