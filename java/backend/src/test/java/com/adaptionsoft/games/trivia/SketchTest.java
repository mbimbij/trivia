package com.adaptionsoft.games.trivia;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class SketchTest {
    @SneakyThrows
    @Test
    @Disabled
    void name() {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> questions = mapper.readValue(Paths.get("src/test/resources/questions-test/Geography.json").toFile(), new TypeReference<>() {
        });
        questions.forEach(question -> {
            Object correctAnswer = ((Map<String, Object>) question.get("availableAnswers")).get(question.get("correctAnswer"));
            question.put("explanations", correctAnswer);
        });
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(questions));
    }
}
