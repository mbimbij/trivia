package com.adaptionsoft.games.utils;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import io.cucumber.datatable.DataTable;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

public class TestUtils {
    public static final Duration pollInterval = Duration.ofMillis(500);
    public static final Duration maxWaitDuration = Duration.ofSeconds(5);

    // TODO adresser les warnings
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true);


    @SneakyThrows
    public static <T> T convertDatatableSingleObject(DataTable dataTable, Class<T> clazz) {
        Map<String, String> map = dataTable.entries().get(0);
        return convertMapToObject(map, clazz);
    }

    @SneakyThrows
    public static <T> T convertDatatableTransposedSingleObject(DataTable dataTable, Class<T> clazz) {
        Map<String, String> map = dataTable.transpose().entries().get(0);
        return convertMapToObject(map, clazz);
    }

    @SneakyThrows
    public static <T> Collection<T> convertDatatableTransposeList(DataTable dataTable, Class<T> clazz) {
        return dataTable.transpose().entries().stream()
                .map(map -> convertMapToObject(map, clazz))
                .toList();
    }

    @SneakyThrows
    public static <T> Collection<T> convertDatatableList(DataTable dataTable, Class<T> clazz) {
        return dataTable.entries().stream()
                .map(map -> convertMapToObject(map, clazz))
                .toList();
    }

    @SneakyThrows
    private static <T> T convertMapToObject(Map<String, String> map, Class<T> clazz) {
        return objectMapper.readValue(JsonUnflattener.unflatten(objectMapper.writeValueAsString(map)), clazz);
    }

    public static String formatInputForWhitespaces(String textContent) {
        return textContent.replace("[TAB]", "\t")
                .replace("[NEWLINE]", "\n");
    }
}
