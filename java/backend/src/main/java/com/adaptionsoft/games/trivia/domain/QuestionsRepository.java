package com.adaptionsoft.games.trivia.domain;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class QuestionsRepository {
    protected String directoryPathString;

    public QuestionsRepository(String directoryPathString) {
        this.directoryPathString = directoryPathString;
    }

    @SneakyThrows
    public QuestionsDeck getQuestions() {
        Path directoryPath = Paths.get(this.directoryPathString);
        try (Stream<Path> files = Files.list(directoryPath)) {
            Map<QuestionsDeck.Category, Queue<Question>> questionsMap = files.map(this::loadQuestionsFromFile)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return new QuestionsDeck(questionsMap);
        }
    }

    protected abstract Map.Entry<QuestionsDeck.Category, Queue<Question>> loadQuestionsFromFile(Path filePath);

    protected String categoryNameFromFilePath(Path filePath) {
        return filePath.toFile().getName().split("\\.")[0];
    }
}
