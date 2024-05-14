package com.adaptionsoft.games.trivia.microarchitecture;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Entity extends EventRaiser {
    @EqualsAndHashCode.Include
    protected Integer id;
}
