package com.adaptionsoft.games.trivia.game.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

public class QuestionsRepositoryJson extends QuestionsRepository {
    private final ObjectMapper mapper;

    public QuestionsRepositoryJson(String directoryPathString) {
        super(directoryPathString);
        this.mapper = new ObjectMapper();
    }

    @SneakyThrows
    @Override
    protected Map.Entry<QuestionsDeck.Category, Queue<Question>> loadQuestionsFromFile(Path filePath) {
        String categoryName = categoryNameFromFilePath(filePath);
        QuestionsDeck.Category questionCategory = Objects.requireNonNull(QuestionsDeck.Category.fromString(categoryName));
        Queue<Question> questions = mapper.readValue(filePath.toFile(), new TypeReference<>() {
        });
        return Map.entry(questionCategory, questions);
    }
}
