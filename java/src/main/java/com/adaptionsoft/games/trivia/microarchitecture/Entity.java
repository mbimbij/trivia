package com.adaptionsoft.games.trivia.microarchitecture;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Entity<T extends Id<?>> extends EventRaiser {
    @EqualsAndHashCode.Include
    protected T id;

    /**
     * DO NOT CALL directly. Needed for @AllArgsConstructor in Player class only.
     */
    protected Entity() {
    }

    public Entity(T id) {
        this.id = id;
    }
}
