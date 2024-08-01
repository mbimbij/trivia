package com.adaptionsoft.games.trivia.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class QuestionsDeck {

    @Getter // for testing only
    private final Map<Category, Queue<Question>> questionsByCategory;
    @Setter
    private QuestionsShuffler shuffler = new RandomQuestionsShuffler();

    public QuestionsDeck(Map<Category, Queue<Question>> questionsByCategory) {
        this.questionsByCategory = new HashMap<>(questionsByCategory);
    }

    Question drawQuestion(int playerLocation) {
        Category category = Category.getQuestionCategory(playerLocation);
        return questionsByCategory.get(category).remove();
    }

    public boolean isValid() {
        return questionsByCategory.keySet().containsAll(Set.of(Category.values()));
    }

    public void shuffle() {
        for (Category c : questionsByCategory.keySet()){
            Queue<Question> shuffledQuestions = this.shuffler.shuffle(questionsByCategory.get(c));
            questionsByCategory.put(c, shuffledQuestions);
        }
    }

    public enum Category {
        // https://www.proprofs.com/quiz-school/story.php?title=thrailkill-hall-pop-culture-quiz
        POP("Pop"),
        SCIENCE("Science"),
        SPORTS("Sports"),
//        ROCK("Rock"),
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

         public static Category getQuestionCategory(int playerLocation) {
            int categoriesCount = values().length;
            return values()[playerLocation % categoriesCount];
        }

        @Override
        public String toString() {
            return value;
        }

    }
}
