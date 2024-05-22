package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.microarchitecture.Id;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserId extends Id<String> {
    public UserId(String value) {
        super(value);
    }
}
