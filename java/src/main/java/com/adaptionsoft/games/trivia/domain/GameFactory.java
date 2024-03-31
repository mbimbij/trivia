package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.GameCreatedEvent;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;

import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.Random;


public class GameFactory {

    private final EventPublisher eventPublisher;
    private final QuestionsLoader questionsLoader;

    public GameFactory(EventPublisher eventPublisher, QuestionsLoader questionsLoader) {
        this.eventPublisher = eventPublisher;
        this.questionsLoader = questionsLoader;
    }

    public Game create(String gameName, String... playersNames) {
        return create(new Random(), gameName, playersNames);
    }

    public Game create(String gameName, Player... players) {
        return create(new Random(), gameName, players);
    }

    public Game create(Random rand, String gameName, String... playersNames) {
        Player[] playersArray = Arrays.stream(playersNames)
                .map(Player::new)
                .toArray(Player[]::new);

        return create(rand, gameName, playersArray);
    }

    public Game create(Random rand, String gameName, Player... playersArg) {
        Questions questions = buildQuestions();
        Players players = new Players(playersArg);

        int squaresCount = 12;
        Board board = new Board(squaresCount);

        Game game = new Game(
                gameName,
                eventPublisher,
                players,
                questions,
                rand,
                board
        );

        eventPublisher.publish(players.getAndClearUncommittedEvents());
        // TODO adresser la gestion d'id avant persistence du jeu en base
        eventPublisher.publish(new GameCreatedEvent(game.getId()));
        return game;
    }


    private Questions buildQuestions() {
        String questionsPath = "src/main/resources/questions";
        Map<Questions.Category, Queue<String>> questionsByCategory = questionsLoader.loadQuestionsFromDirectory(questionsPath);
        return new Questions(questionsByCategory);
    }
}
