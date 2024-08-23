package com.adaptionsoft.games.trivia.shared.microarchitecture;

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
        super(null);
    }

    public Entity(T id, EventPublisher eventPublisher) {
        super(eventPublisher);
        this.id = id;
    }
}
