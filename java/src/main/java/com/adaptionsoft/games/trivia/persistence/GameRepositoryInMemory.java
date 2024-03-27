package com.adaptionsoft.games.trivia.persistence;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameRepositoryInMemory implements GameRepository {
    private final List<Game> games = new ArrayList<>();

    @Override
    public Game save(Game game) {
        games.add(game);
        return game;
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
