package com.adaptionsoft.games.trivia.domain;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuestionsLoader {


    private String directoryPathString;

    public QuestionsLoader(String directoryPathString) {
        this.directoryPathString = directoryPathString;
    }

    @SneakyThrows
    public Map<Questions.Category, Queue<String>> loadQuestionsFromDirectory() {
        Path directoryPath = Paths.get(this.directoryPathString);
        try (Stream<Path> files = Files.list(directoryPath)) {
            return files.map(this::loadQuestionsFromFile)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

    @SneakyThrows
    private Map.Entry<Questions.Category, Queue<String>> loadQuestionsFromFile(Path filePath) {
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            Queue<String> questions = new ArrayDeque<>(lines.toList());
            String categoryName = categoryNameFromFilePath(filePath);
            Questions.Category questionCategory = Objects.requireNonNull(Questions.Category.fromString(categoryName));
            return Map.entry(questionCategory, questions);
        }
    }

    private String categoryNameFromFilePath(Path filePath) {
        return filePath.toFile().getName().split("\\.")[0];
    }
}
