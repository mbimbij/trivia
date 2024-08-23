package com.adaptionsoft.games.trivia.game.infra;

import com.adaptionsoft.games.trivia.game.domain.Game;
import com.adaptionsoft.games.trivia.game.domain.GameId;
import com.adaptionsoft.games.trivia.game.domain.GameRepository;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class GameRepositoryInMemory implements GameRepository {
    private final List<Game> games = new ArrayList<>();

    @Override
    public void save(Game game) {
        if (!games.contains(game)) {
            games.add(game);
        }
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
    public Optional<Game> findById(GameId gameId) {
        return games.stream()
                .filter(game -> Objects.equals(gameId, game.getId()))
                .findAny();
    }

    @Override
    public void deleteGameById(GameId gameId) {
        this.findById(gameId).ifPresent(games::remove);
    }
}
