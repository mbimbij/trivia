package com.adaptionsoft.games.trivia.domain;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public enum QuestionCategory {
    POP("Pop"),
    SCIENCE("Science"),
    SPORTS("Sports"),
    ROCK("Rock"),
    GEOGRAPHY("Geography"),
    ;
    private final Queue<String> cards = new ArrayDeque<>();
    private final String value;

    QuestionCategory(String value) {
        this.value = value;
    }

    static QuestionCategory fromString(String text) {
        for (QuestionCategory category : QuestionCategory.values()) {
            if(category.value.equalsIgnoreCase(text)){
                return category;
            }
        }
        return null;
    }

    static void clearDeck() {
        Arrays.stream(values())
                .forEach(QuestionCategory::clear);
    }

    public void stackCard(String content) {
        cards.add(content);
    }

    public String drawCard() {
        return cards.remove();
    }

    @Override
    public String toString() {
        return value;
    }

    public void clear() {
        cards.clear();
    }
}
