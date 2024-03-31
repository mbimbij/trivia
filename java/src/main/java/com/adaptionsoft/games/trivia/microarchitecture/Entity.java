package com.adaptionsoft.games.trivia.microarchitecture;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Entity extends EventRaiser {
    protected Integer id;
}
