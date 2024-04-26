package com.adaptionsoft.games.trivia.infra;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameRepository;
import com.adaptionsoft.games.trivia.domain.Player;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class GameRepositoryInMemory implements GameRepository {
    private final Set<Game> games = new HashSet<>();
    private final IdGenerator idGenerator;

    @Override
    public void save(Game game) {
        if(game.getId() == null) {
            game.setId(idGenerator.nextId());
        }
        games.add(game);
    }

    @Override
    public Collection<Game> list() {
        return new ArrayList<>(games);
    }

    @Override
    public void deleteAll() {
        games.clear();
    }

    @Override
    public Optional<Game> findById(int gameId) {
        return games.stream()
                .filter(game -> Objects.equals(gameId, game.getId()))
                .findAny();
    }
}
