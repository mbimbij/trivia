package com.adaptionsoft.games.trivia.domain;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class QuestionsRepositoryTxt extends QuestionsRepository {

    public QuestionsRepositoryTxt(String directoryPathString) {
        super(directoryPathString);
    }

    @Override
    @SneakyThrows
    protected Map.Entry<QuestionsDeck.Category, Queue<Question>> loadQuestionsFromFile(Path filePath) {
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            Queue<Question> questions = new ArrayDeque<>(lines
                    .map(s ->
                            new Question("id", s, Collections.emptyMap(), AnswerCode.A))
                    .toList()
            );
            String categoryName = categoryNameFromFilePath(filePath);
            QuestionsDeck.Category questionCategory = Objects.requireNonNull(QuestionsDeck.Category.fromString(categoryName));
            return Map.entry(questionCategory, questions);
        }
    }

}
