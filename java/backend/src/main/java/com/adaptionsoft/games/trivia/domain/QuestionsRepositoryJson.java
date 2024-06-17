package com.adaptionsoft.games.trivia.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class QuestionsRepositoryJson extends QuestionsRepository {
    private final ObjectMapper mapper;

    public QuestionsRepositoryJson(String directoryPathString) {
        super(directoryPathString);
        this.mapper = new ObjectMapper();
    }

    @SneakyThrows
    @Override
    protected Map.Entry<Questions.Category, Queue<Question>> loadQuestionsFromFile(Path filePath) {
        String categoryName = categoryNameFromFilePath(filePath);
        Questions.Category questionCategory = Objects.requireNonNull(Questions.Category.fromString(categoryName));
        Queue<Question> questions = mapper.readValue(filePath.toFile(), new TypeReference<Queue<Question>>() {
        });
        return Map.entry(questionCategory, questions);
    }
}
