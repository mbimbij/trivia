package com.adaptionsoft.games.trivia.microarchitecture;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Entity extends EventRaiser {
    protected Integer id;
}
