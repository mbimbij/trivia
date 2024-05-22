package com.adaptionsoft.games.trivia.domain;

import java.util.Collection;
import java.util.Optional;

public interface GameRepository {
    void save(Game game);
    Collection<Game> list();
    void deleteAll();

    Optional<Game> findById(GameId gameId);

    void deleteGameById(GameId gameId);
}
