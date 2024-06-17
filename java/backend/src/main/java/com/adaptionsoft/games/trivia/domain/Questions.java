package com.adaptionsoft.games.trivia.domain;

import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Questions {

    private final Map<Category, Queue<Question>> questionsByCategory;

    public Questions(Map<Category, Queue<Question>> questionsByCategory) {
        this.questionsByCategory = questionsByCategory;
    }

    String drawQuestion(int playerLocation) {
        Category category = Category.getQuestionCategory(playerLocation);
        return questionsByCategory.get(category).remove().questionText();
    }

    public boolean isValid() {
        return questionsByCategory.keySet().containsAll(Set.of(Category.values()));
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
