package com.adaptionsoft.games.trivia.game.domain;

import com.adaptionsoft.games.trivia.shared.microarchitecture.Id;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class GameId extends Id<Integer> {
    public GameId(Integer value) {
        super(value);
    }
}
