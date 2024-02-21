package com.adaptionsoft.games.trivia.domain;

@FunctionalInterface
public interface AnsweringStrategy {
    boolean isAnsweringCorrectly();
}
