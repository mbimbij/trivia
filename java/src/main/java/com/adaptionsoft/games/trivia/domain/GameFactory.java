package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.GameCreatedEvent;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;

import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.Random;


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
                .map(Player::new)
                .toArray(Player[]::new);
        Players players = new Players(playersArray);
        eventPublisher.publish(players.getAndClearUncommittedEvents());

        Game game = new Game(
                eventPublisher,
                players,
                questions,
                squaresCount,
                rand
        );

        eventPublisher.publish(new GameCreatedEvent());
        return game;
    }

    private Questions buildQuestions() {
        String questionsPath = "src/main/resources/questions";
        Map<Questions.Category, Queue<String>> questionsByCategory = questionsLoader.loadQuestionsFromDirectory(questionsPath);
        return new Questions(questionsByCategory);
    }
}
