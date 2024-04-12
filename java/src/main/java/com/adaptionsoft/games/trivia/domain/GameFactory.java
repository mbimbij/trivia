package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.GameCreatedEvent;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import static com.adaptionsoft.games.trivia.domain.Game.State.CREATED;


public class GameFactory {

    private final EventPublisher eventPublisher;
    private final QuestionsLoader questionsLoader;

    public GameFactory(EventPublisher eventPublisher, QuestionsLoader questionsLoader) {
        this.eventPublisher = eventPublisher;
        this.questionsLoader = questionsLoader;
    }

    public Game create(String gameName, String creatorName, String... otherPlayersNames) {
        return create(new Random(), gameName, creatorName, otherPlayersNames);
    }

    public Game create(String gameName, Player creator, Player... players) {
        return create(new Random(), gameName, creator, players);
    }

    public Game create(Random rand, String gameName, @NonNull String creatorName, String... playersNames) {
        Player[] playersArray = Arrays.stream(playersNames)
                .map(Player::new)
                .toArray(Player[]::new);

        final Player player = new Player(creatorName);
        return create(rand, gameName, player, playersArray);
    }

    public Game create(Random rand, String gameName, @NonNull Player creator, Player... otherPlayers) {
        Questions questions = buildQuestions();
        Players players = PlayersFactory.create(creator, otherPlayers);

        int squaresCount = 12;
        Board board = new Board(squaresCount);

        Game game = new Game(
                gameName,
                eventPublisher,
                players,
                new PlayerTurnOrchestrator(questions, rand, board), players.getCurrent(), CREATED
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
