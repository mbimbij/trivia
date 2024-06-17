package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.GameCreatedEvent;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Random;

import static com.adaptionsoft.games.trivia.domain.Game.State.CREATED;


public class GameFactory {
    private final IdGenerator idGenerator;
    private final EventPublisher eventPublisher;
    private final QuestionsRepository questionsRepository;

    public Game create(String gameName, String creatorName, String... otherPlayersNames) {
        return create(new Random(), gameName, creatorName, otherPlayersNames);
    }

    public GameFactory(IdGenerator idGenerator, EventPublisher eventPublisher, QuestionsRepository questionsRepository) {
        this.idGenerator = idGenerator;
        this.eventPublisher = eventPublisher;
        this.questionsRepository = questionsRepository;
    }

    public Game create(String gameName, Player creator, Player... players) {
        return create(new Random(), gameName, creator, players);
    }

    public Game create(Random rand, String gameName, @NonNull String creatorName, String... playersNames) {
        Player[] playersArray = Arrays.stream(playersNames)
                .map((String playerName) -> new Player(new UserId(playerName),playerName))
                .toArray(Player[]::new);

        final Player player = new Player(new UserId(creatorName), creatorName);
        return create(rand, gameName, player, playersArray);
    }

    public Game create(Random rand, String gameName, @NonNull Player creator, Player... otherPlayers) {
        Questions questions = questionsRepository.getQuestions();
        Players players = PlayersFactory.create(creator, otherPlayers);

        int squaresCount = 12;
        Board board = new Board(squaresCount);

        Integer id = idGenerator.nextId();
        Game game = new Game(
                new GameId(id),
                gameName,
                eventPublisher,
                players,
                new PlayerTurnOrchestrator(questions, rand, board),
                players.getCurrent(),
                CREATED,
                questions
        );

        eventPublisher.publish(players.getAndClearUncommittedEvents());
        eventPublisher.publish(new GameCreatedEvent(game.getId()));
        return game;
    }


}
