package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.GameRepository;
import com.adaptionsoft.games.trivia.domain.Player;
import com.adaptionsoft.games.trivia.domain.TestFixtures;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import com.adaptionsoft.games.trivia.web.CreateGameRequestDto;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.assertj.core.condition.AllOf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collection;
import java.util.List;

import static com.adaptionsoft.games.trivia.domain.Game.State.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.condition.AllOf.allOf;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
//@WebMvcTest(TriviaController.class)
class TriviaApplicationShould {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private GameRepository gameRepository;
    @SpyBean
    private IdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
        Mockito.reset(idGenerator);
    }

    @Nested
    class ListGames {
        @SneakyThrows
        @Test
        void return_empty_response__when_no_game() {
            // WHEN
            ResultActions perform = mvc.perform(get("/game"));

            // THEN http status is ok
            MvcResult mvcResult = perform
                    .andExpect(status().isOk())
                    .andReturn();

            // and response is empty
            String responseString = mvcResult.getResponse().getContentAsString();
            Collection<GameResponseDto> response = mapper.readValue(responseString, new TypeReference<>() {
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
            Collection<GameResponseDto> response = mapper.readValue(responseString, new TypeReference<>() {
            });
            assertThat(response)
                    .singleElement()
                    .usingRecursiveComparison()
                    .isEqualTo(GameResponseDto.from(existingGame));
        }
    }

    @Nested
    class CreateGame {
        @SneakyThrows
        @Test
        void can_create_game() {
            // GIVEN a request
            int creatorId = 1;
            String creatorName = "player name";
            UserDto creatorDto = new UserDto(creatorId, creatorName);

            String gameName = "game name";
            CreateGameRequestDto requestDto = new CreateGameRequestDto(gameName, creatorDto);

            // AND a set id generation strategy
            int gameId = 2;
            Mockito.doReturn(gameId).when(idGenerator).nextId();

            // WHEN I create a game
            ResultActions performResultActions = mvc.perform(post("/game")
                            .content(mapper.writeValueAsString(requestDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // THEN the http response is as expected
            ResultActions statusVerifyResultActions = performResultActions.andExpect(status().isCreated());
            GameResponseDto actualResponseDto = mapper.readValue(
                    statusVerifyResultActions.andReturn().getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });
            GameResponseDto expectedResponseDto = new GameResponseDto(gameId, gameName, CREATED.toString(), creatorDto, List.of(creatorDto));
            assertThat(actualResponseDto).usingRecursiveComparison().isEqualTo(expectedResponseDto);

            // AND the game is created
            assertThat(gameRepository.getById(gameId))
                    .hasValueSatisfying(game -> {
                        assertThat(game.getId()).isEqualTo(gameId);
                        assertThat(game.getName()).isEqualTo(gameName);
                        Player expectedCreator = new Player(creatorId, creatorName);
                        assertThat(game.getPlayers().getCreator()).isEqualTo(expectedCreator);
                        assertThat(game.getPlayers().getIndividualPlayers()).containsExactly(expectedCreator);
                    });
        }
    }

}
