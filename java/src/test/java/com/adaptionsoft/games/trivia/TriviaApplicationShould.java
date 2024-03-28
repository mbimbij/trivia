package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.domain.*;
import com.adaptionsoft.games.trivia.web.GameDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
//@WebMvcTest(TriviaController.class)
class TriviaApplicationShould {

    @Autowired
    private MockMvc mvc;
    @Autowired
    GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void return_empty_response__when_no_game() {
        MvcResult mvcResult = mvc.perform(get("/game"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        Collection<GameDto> response = new ObjectMapper().readValue(responseString, new TypeReference<>() {
        });

        assertThat(response).isEmpty();
    }

    @SneakyThrows
    @Test
    void return_single_game_response__when_one_game_created() {
        // GIVEN an existing game
        Game existingGame = TestFixtures.a2playersGame();
        gameRepository.save(existingGame);


        // WHEN listing game
        ResultActions resultActions = mvc.perform(get("/game"));

        // THEN status is ok
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andReturn();

        // AND the content is as expected
        String responseString = mvcResult.getResponse().getContentAsString();
        Collection<GameDto> response = new ObjectMapper().readValue(responseString, new TypeReference<>() {
        });
        assertThat(response).hasSize(1)
                .singleElement()
                .usingRecursiveComparison()
                .isEqualTo(GameDto.from(existingGame));
    }

}
