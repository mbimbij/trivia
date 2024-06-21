package com.adaptionsoft.games.trivia.application;

import com.adaptionsoft.games.trivia.domain.*;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import com.adaptionsoft.games.trivia.web.*;
import com.adaptionsoft.games.trivia.websocket.WebSocketConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collection;
import java.util.List;

import static com.adaptionsoft.games.trivia.domain.State.CREATED;
import static com.adaptionsoft.games.trivia.domain.State.STARTED;
import static com.adaptionsoft.games.trivia.domain.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TriviaController.class)
@ActiveProfiles("test")
@Import(WebSocketConfig.class)
class TriviaApplicationShould {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameFactory gameFactory;
    @Autowired
    private PlayerFactory playerFactory;
    @SpyBean
    private IdGenerator idGenerator;
    private Player player1;
    private Player player2;
    private @NotBlank PlayerDto player1Dto;
    private @NotBlank PlayerDto player2Dto;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
        Mockito.reset(idGenerator);
        player1 = player1();
        player2 = player2();
        player1Dto = PlayerDto.from(player1);
        player2Dto = PlayerDto.from(player2);
    }

    @SneakyThrows
    @Test
    void return_empty_response__when_no_game() {
        // WHEN
        ResultActions perform = mvc.perform(get("/api/games"));

        // THEN http status is ok
        MvcResult mvcResult = perform
                .andExpect(status().isOk())
                .andReturn();

        // and response is empty
        String responseString = mvcResult.getResponse().getContentAsString();
        Collection<GameResponseDto> response = mapper.readValue(responseString,
                new TypeReference<>() {
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
        ResultActions resultActions = mvc.perform(get("/api/games"));

        // THEN status is ok
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andReturn();

        // AND the content is as expected
        String responseString = mvcResult.getResponse().getContentAsString();
        Collection<GameResponseDto> response = mapper.readValue(responseString,
                new TypeReference<>() {
                });
        assertThat(response)
                .singleElement()
                .usingRecursiveComparison()
                .isEqualTo(GameResponseDto.from(existingGame));
    }

    @SneakyThrows
    @Test
    void user_can_create_game() {
        // GIVEN a request
        @NotBlank PlayerDto player1Dto = PlayerDto.from(player1);
        @NotBlank UserDto user1Dto = new UserDto(player1Dto.id(), player1Dto.name());

        String gameName = "game name";
        CreateGameRequestDto requestDto = new CreateGameRequestDto(gameName, user1Dto);

        // AND a set id generation strategy
        int gameIdInt = 2;
        Mockito.doReturn(gameIdInt).when(idGenerator).nextId();

        // WHEN I create a game
        ResultActions performResultActions = mvc.perform(post("/api/games")
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // THEN the http response is as expected
        ResultActions statusVerifyResultActions = performResultActions.andExpect(status().isCreated());
        GameResponseDto actualResponseDto = mapper.readValue(
                statusVerifyResultActions.andReturn().getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        GameResponseDto expectedResponseDto = new GameResponseDto(gameIdInt,
                gameName,
                CREATED.toString(),
                0,
                player1Dto,
                List.of(player1Dto),
                player1Dto,
                null,
                null
        );
        assertThat(actualResponseDto).usingRecursiveComparison().isEqualTo(expectedResponseDto);

        // AND the game is created
        Player expectedCreator = player1();
        GameId gameId = new GameId(gameIdInt);
        expectedCreator.setGameId(gameId);

        assertThat(gameRepository.findById(gameId))
                .hasValueSatisfying(game -> {
                    assertThat(game.getId()).isEqualTo(gameId);
                    assertThat(game.getName()).isEqualTo(gameName);
                    assertThat(game.getCreator()).isEqualTo(expectedCreator);
                    assertThat(game.getCreator().getGameId()).isEqualTo(gameId);
                    assertThat(game.getPlayersList()).containsExactly(expectedCreator);
                });
    }

    @SneakyThrows
    @Test
    void user_can_join_game() {
        // GIVEN an existing game
        Game game = gameFactory.create("game name", player1);
        gameRepository.save(game);

        // WHEN a new player joins the game
        @NotBlank PlayerDto newPlayerDto = PlayerDto.from(player2());
        ResultActions resultActions = mvc.perform(
                post("/api/games/{gameId}/players/{playerId}/join",
                        game.getId().getValue(),
                        newPlayerDto.id())
                        .content(mapper.writeValueAsString(newPlayerDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // THEN the http response is as expected
        ResultActions statusVerifyResultActions = resultActions.andExpect(status().isCreated());
        GameResponseDto actualResponseDto = mapper.readValue(
                statusVerifyResultActions.andReturn().getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        @NotBlank PlayerDto creatorDto = PlayerDto.from(player1);
        GameResponseDto expectedResponseDto = new GameResponseDto(game.getId().getValue(),
                game.getName(),
                CREATED.toString(),
                0,
                creatorDto,
                List.of(creatorDto, newPlayerDto),
                creatorDto,
                null,
                null);
        assertThat(actualResponseDto).usingRecursiveComparison().isEqualTo(expectedResponseDto);

        // AND the player is added to the game
        assertThat(gameRepository.findById(game.getId()))
                .hasValueSatisfying(g -> {
                    assertThat(g.getPlayersCount()).isEqualTo(2);
                    assertThat(g.getPlayersList()).contains(playerFactory.fromDto(newPlayerDto));
                });
    }

    @SneakyThrows
    @Test
    void creator_can_start_game() {
        // GIVEN an existing game
        Game game = gameFactory.create("game name", player1, player2);
        gameRepository.save(game);

        // WHEN the creator starts the game
        ResultActions resultActions = mvc.perform(
                post("/api/games/{gameId}/players/{playerId}/start",
                        game.getId().getValue(),
                        player1.getId().getValue())
        );

        // THEN the response status is ok
        ResultActions statusVerifyResultActions = resultActions.andExpect(status().isOk());

        // AND the response body is as expected
        GameResponseDto actualResponseDto = mapper.readValue(
                statusVerifyResultActions.andReturn().getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        GameResponseDto expectedResponseDto = new GameResponseDto(game.getId().getValue(),
                game.getName(),
                STARTED.toString(),
                1,
                player1Dto,
                List.of(player1Dto, player2Dto),
                player1Dto, null, null);
        assertThat(actualResponseDto).usingRecursiveComparison().isEqualTo(expectedResponseDto);
    }

    @SneakyThrows
    @Test
    void can_roll_dice() {
        // GIVEN a started game
        Game game = gameFactory.create("game", player1, player2);
        game.setDice(new LoadedDice(3));
        game.setQuestionsDeck(loadedQuestionsDeck());
        game.start(player1);
        gameRepository.save(game);

        // WHEN player1 rolls the dice
        ResultActions resultActions = mvc.perform(
                post("/api/games/{gameId}/players/{playerId}/rollDice",
                        game.getId().getValue(),
                        player1.getId().getValue())
        );

        // THEN the response status is CREATED
        MvcResult result = resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // AND the response body is as expected
        GameResponseDto actualResponseDto = mapper.readValue(result.getResponse().getContentAsString(), GameResponseDto.class);
        QuestionDto expectedCurrentQuestionDto = QuestionDto.from(questionTest());

        GameResponseDto expectedResponseDto = new GameResponseDto(game.getId().getValue(),
                game.getName(),
                STARTED.toString(),
                1,
                player1Dto,
                List.of(player1Dto, player2Dto),
                player1Dto, expectedCurrentQuestionDto, 3);
        assertThat(actualResponseDto).usingRecursiveComparison().isEqualTo(expectedResponseDto);

    }

}
