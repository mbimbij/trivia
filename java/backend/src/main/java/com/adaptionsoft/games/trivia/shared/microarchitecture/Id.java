package com.adaptionsoft.games.trivia.shared.microarchitecture;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
public abstract class Id<T> {
    private final T value;

    protected Id(T value) {
        this.value = value;
    }
}
