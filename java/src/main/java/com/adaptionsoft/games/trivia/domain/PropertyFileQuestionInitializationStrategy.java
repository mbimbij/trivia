package com.adaptionsoft.games.trivia.domain;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PropertyFileQuestionInitializationStrategy implements QuestionInitializationStrategy {
    @Override
    public void run() {
        QuestionCategory.clearDeck();
        Map<QuestionCategory, List<String>> questions = loadQuestionsFromDirectory("src/main/resources/questions");
        questions.entrySet()
                .stream()
                .forEach(entry -> entry.getValue()
                        .forEach(question -> entry.getKey().stackCard(question))
                );
    }

    @SneakyThrows
    public Map<QuestionCategory, List<String>> loadQuestionsFromDirectory(String directoryPathString) {
        Path directoryPath = Paths.get(directoryPathString);
        try (Stream<Path> files = Files.list(directoryPath)) {
            return files.map(this::loadQuestionsFromFile)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

    @SneakyThrows
    private Map.Entry<QuestionCategory, List<String>> loadQuestionsFromFile(Path filePath) {
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            List<String> questions = lines.toList();
            String categoryName = categoryNameFromFilePath(filePath);
            QuestionCategory questionCategory = Objects.requireNonNull(QuestionCategory.fromString(categoryName));
            return Map.entry(questionCategory, questions);
        }
    }

    private String categoryNameFromFilePath(Path filePath) {
        return filePath.toFile().getName().split("\\.")[0];
    }
}
