package com.adaptionsoft.games.trivia.domain;

import java.util.Collection;

public interface GameRepository {
    void save(Game game);
    Collection<Game> list();
    void deleteAll();
}
