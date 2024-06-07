package com.adaptionsoft.games.trivia.application;

import com.adaptionsoft.games.trivia.domain.*;
import com.adaptionsoft.games.trivia.microarchitecture.IdGenerator;
import com.adaptionsoft.games.trivia.web.*;
import com.adaptionsoft.games.trivia.websocket.WebSocketConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.SneakyThrows;
import org.apache.catalina.User;
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

import static com.adaptionsoft.games.trivia.domain.Game.State.CREATED;
import static com.adaptionsoft.games.trivia.domain.Game.State.STARTED;
import static com.adaptionsoft.games.trivia.domain.TestFixtures.player1;
import static com.adaptionsoft.games.trivia.domain.TestFixtures.player2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    @SpyBean
    private IdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
        Mockito.reset(idGenerator);
    }

    @SneakyThrows
    @Test
    void return_empty_response__when_no_game() {
        // WHEN
        ResultActions perform = mvc.perform(get("/games"));

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
        ResultActions resultActions = mvc.perform(get("/games"));

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

    @SneakyThrows
    @Test
    void user_can_create_game() {
        // GIVEN a request
        String playerId = player1().getId().getValue();
        String playerName = player1().getName();
        @NotBlank PlayerDto creatorDtoPlayer = new PlayerDto(playerId, playerName);
        @NotBlank UserDto creatorDtoUser = new UserDto(playerId, playerName);

        String gameName = "game name";
        CreateGameRequestDto requestDto = new CreateGameRequestDto(gameName, creatorDtoUser);

        // AND a set id generation strategy
        int gameIdInt = 2;
        Mockito.doReturn(gameIdInt).when(idGenerator).nextId();

        // WHEN I create a game
        ResultActions performResultActions = mvc.perform(post("/games")
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
                creatorDtoPlayer,
                List.of(creatorDtoPlayer),
                creatorDtoPlayer
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
        Player creator = player1();
        Game game = gameFactory.create("game name", creator);
        gameRepository.save(game);

        // WHEN a new player joins the game
        @NotBlank PlayerDto newPlayerDto = PlayerDto.from(player2());
        ResultActions resultActions = mvc.perform(
                post("/games/{gameId}/players/{playerId}/join", game.getId().getValue(), newPlayerDto.id())
                        .content(mapper.writeValueAsString(newPlayerDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // THEN the http response is as expected
        ResultActions statusVerifyResultActions = resultActions.andExpect(status().isCreated());
        GameResponseDto actualResponseDto = mapper.readValue(
                statusVerifyResultActions.andReturn().getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        @NotBlank PlayerDto creatorDto = PlayerDto.from(creator);
        GameResponseDto expectedResponseDto = new GameResponseDto(game.getId().getValue(),
                game.getName(),
                CREATED.toString(),
                0,
                creatorDto,
                List.of(creatorDto, newPlayerDto),
                creatorDto);
        assertThat(actualResponseDto).usingRecursiveComparison().isEqualTo(expectedResponseDto);

        // AND the player is added to the game
        assertThat(gameRepository.findById(game.getId()))
                .hasValueSatisfying(g -> {
                    assertThat(g.getPlayersCount()).isEqualTo(2);
                    assertThat(g.getPlayersList()).contains(newPlayerDto.toDomainObject());
                });
    }

    @SneakyThrows
    @Test
    void creator_can_start_game() {
        // GIVEN an existing game
        Player player1 = player1();
        Player player2 = player2();
        final Player[] players = new Player[]{player1, player2};
        Game game = gameFactory.create("game name", player1, player2);
        gameRepository.save(game);

        // WHEN the creator starts the game
        ResultActions resultActions = mvc.perform(
                post("/games/{gameId}/players/{playerId}/start", game.getId().getValue(), player1.getId().getValue())
        );

        // THEN the response status is ok
        ResultActions statusVerifyResultActions = resultActions.andExpect(status().isOk());

        // AND the response body is as expected
        GameResponseDto actualResponseDto = mapper.readValue(
                statusVerifyResultActions.andReturn().getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        @NotBlank PlayerDto creatorDto = PlayerDto.from(player1);
        @NotBlank PlayerDto player2Dto = PlayerDto.from(player2);
        GameResponseDto expectedResponseDto = new GameResponseDto(game.getId().getValue(),
                game.getName(),
                STARTED.toString(),
                1,
                creatorDto,
                List.of(creatorDto, player2Dto),
                creatorDto);
        assertThat(actualResponseDto).usingRecursiveComparison().isEqualTo(expectedResponseDto);
    }

    @SneakyThrows
    @Test
    void let_current_player_play_turn() {
        // GIVEN an existing started game
        Player player1 = player1();
        Player player2 = player2();
        final Player[] players = new Player[]{player1, player2};
        Game game = gameFactory.create("game name", player1, player2);
        gameRepository.save(game);
        game.startBy(player1);

        // WHEN the current player (creator) starts the game
        ResultActions resultActions = mvc.perform(
                post("/games/{gameId}/players/{playerId}/playTurn", game.getId().getValue(), player1.getId().getValue())
        );

        // THEN response status is ok
        ResultActions statusVerifyResultActions = resultActions.andExpect(status().isOk());

        // AND the response body is as expected
        GameResponseDto actualResponseDto = mapper.readValue(
                statusVerifyResultActions.andReturn().getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        @NotBlank PlayerDto creatorDto = PlayerDto.from(player1);
        @NotBlank PlayerDto otherPlayerDto = PlayerDto.from(player2);
        GameResponseDto expectedResponseDto = new GameResponseDto(game.getId().getValue(),
                game.getName(),
                STARTED.toString(),
                2,
                creatorDto,
                List.of(creatorDto, otherPlayerDto),
                otherPlayerDto);
        assertThat(actualResponseDto).usingRecursiveComparison().isEqualTo(expectedResponseDto);
    }
}
