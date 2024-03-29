package com.adaptionsoft.games.trivia.infra;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameRepository;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class GameRepositoryInMemory implements GameRepository {
    private final List<Game> games = new ArrayList<>();
    private final IdGenerator idGenerator;

    @Override
    public void save(Game game) {
        game.setId(idGenerator.nextId());
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
}
