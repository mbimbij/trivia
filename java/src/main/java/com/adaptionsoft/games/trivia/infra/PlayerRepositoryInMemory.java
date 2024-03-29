package com.adaptionsoft.games.trivia.infra;

import com.adaptionsoft.games.trivia.domain.Player;
import com.adaptionsoft.games.trivia.domain.PlayerRepository;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PlayerRepositoryInMemory implements PlayerRepository {
    private final List<Player> players = new ArrayList<>();
    private final IdGenerator idGenerator;
    @Override
    public void save(Player player) {
        player.setId(idGenerator.nextId());
        players.add(player);
    }
}
