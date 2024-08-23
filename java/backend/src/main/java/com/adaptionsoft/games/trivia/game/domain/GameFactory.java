package com.adaptionsoft.games.trivia.game.domain;

import com.adaptionsoft.games.trivia.game.domain.event.GameCreatedEvent;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventPublisher;
import com.adaptionsoft.games.trivia.shared.microarchitecture.IdGenerator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Random;

import static com.adaptionsoft.games.trivia.game.domain.GameState.CREATED;

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

        // TODO: récupérer depuis des properties ?
        int squaresCount = 12;
        Board board = new Board(squaresCount);

        QuestionsDeck questions = questionsRepository.getQuestions();

        Game game = new Game(
                gameId,
                gameName,
                CREATED,
                eventPublisher,
                board,
                new Dice(rand),
                questions,
                creator,
                otherPlayers
        );

        eventPublisher.raise(new GameCreatedEvent(game.getId()));
        eventPublisher.flush();
        return game;
    }
}
