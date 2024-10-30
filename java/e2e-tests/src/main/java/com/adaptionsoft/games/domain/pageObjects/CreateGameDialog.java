package com.adaptionsoft.games.domain.pageObjects;

import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.trivia.game.web.GameResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.WebSocket;
import lombok.SneakyThrows;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CreateGameDialog extends Dialog {
    public static final String OPEN_DIALOG_BUTTON = "create-game";
    public static final String DIALOG = "create-game-dialog";
    public static final String GAME_NAME = "game-name";
    public static final String CREATOR_NAME = "creator-name";
    private final String backendWebsocketUrl;
    private final TestContext testContext;
    private final ObjectMapper objectMapper;

    public CreateGameDialog(Page page, String backendWebsocketUrl, TestContext testContext, ObjectMapper objectMapper) {
        super(page, DIALOG);
        this.backendWebsocketUrl = backendWebsocketUrl;
        this.testContext = testContext;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public int createGame(String gameName) {
        verifyAbsence();
        clickOpenDialogButton();
        verifyPresence();
        fillInputByTestId(GAME_NAME, gameName);
        int newGameId = clickValidateAndGetGameIdBack();
        verifyAbsence();
        return newGameId;
    }

    public int clickValidateAndGetGameIdBack() {
        AtomicInteger gameIdAtomicInt = new AtomicInteger();
        WebSocket webSocket = testContext.getGameListPageWebSocket();

        webSocket.waitForFrameReceived(new WebSocket.WaitForFrameReceivedOptions().setPredicate(webSocketFrame -> {
                    String text = webSocketFrame.text();
                    if (text.contains("destination:/topic/games/created")) {
                        try {
                            String gameString = text.split("\n\n")[1];
                            GameResponseDto gameResponseDto = objectMapper.readValue(gameString, GameResponseDto.class);
                            gameIdAtomicInt.set(gameResponseDto.id());
                            return true;
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return false;
                }),
                () -> this.clickButtonByTestId(VALIDATE)
        );

        return gameIdAtomicInt.get();
    }

    // TODO ajouter un test de création de partie depuis le frontend
    @SneakyThrows
    public int createGame(String gameName, String creatorName) {
        verifyAbsence();
        clickOpenDialogButton();
        verifyPresence();
        fillInputByTestId(GAME_NAME, gameName);
        fillInputByTestId(CREATOR_NAME, creatorName);
        int newGameId = clickValidateAndGetGameIdBack();
        verifyAbsence();
        return newGameId;
    }

}
