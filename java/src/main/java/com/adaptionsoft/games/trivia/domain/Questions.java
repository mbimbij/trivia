package com.adaptionsoft.games.trivia.domain;

import java.util.Map;
import java.util.Queue;

public class Questions {

    private final Map<Category, Queue<String>> questionsByCategory;

    public Questions(Map<Category, Queue<String>> questionsByCategory) {
        this.questionsByCategory = questionsByCategory;
    }

    String drawQuestion(int playerLocation) {
        Category category = Category.getQuestionCategory(playerLocation);
        return questionsByCategory.get(category).remove();
    }

    public enum Category {
        POP("Pop"),
        SCIENCE("Science"),
        SPORTS("Sports"),
        ROCK("Rock"),
        GEOGRAPHY("Geography"),
        ;
        private final String value;

        Category(String value) {
            this.value = value;
        }

        static Category fromString(String text) {
            for (Category category : Category.values()) {
                if(category.value.equalsIgnoreCase(text)){
                    return category;
                }
            }
            return null;
        }

        static Category getQuestionCategory(int playerLocation) {
            int categoriesCount = values().length;
            return values()[playerLocation % categoriesCount];
        }

        @Override
        public String toString() {
            return value;
        }

    }
}
