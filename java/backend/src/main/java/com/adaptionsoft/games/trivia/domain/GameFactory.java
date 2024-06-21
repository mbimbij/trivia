package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.GameCreatedEvent;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Random;

import static com.adaptionsoft.games.trivia.domain.State.CREATED;

@RequiredArgsConstructor
public class GameFactory {
    private final IdGenerator idGenerator;
    private final EventPublisher eventPublisher;
    private final QuestionsRepository questionsRepository;

    public Game create(String gameName, Player creator, Player... players) {
        return create(new Random(), gameName, creator, players);
    }

    public Game create(Random rand, String gameName, @NonNull Player creator, Player... otherPlayers) {
        GameId gameId = new GameId(idGenerator.nextId());
        creator.setGameId(gameId);
        Arrays.stream(otherPlayers).forEach(player -> {
            player.setGameId(gameId);
        });

        Players players = PlayersFactory.create(eventPublisher, creator, otherPlayers);

        int squaresCount = 12;
        Board board = new Board(squaresCount);

        Questions questions = questionsRepository.getQuestions();

        Game game = new Game(
                gameId,
                gameName,
                eventPublisher,
                players,
                players.getCurrent(),
                board,
                new Dice(rand),
                CREATED, questions
        );

        eventPublisher.raise(new GameCreatedEvent(game.getId()));
        eventPublisher.flushEvents();
        return game;
    }
}
