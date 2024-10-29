package com.adaptionsoft.games.domain.pageObjects;

import com.adaptionsoft.games.domain.TestContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.WebSocket;
import lombok.SneakyThrows;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class CreateGameDialog extends Dialog {
    public static final String OPEN_DIALOG_BUTTON = "create-game";
    public static final String DIALOG = "create-game-dialog";
    public static final String GAME_NAME = "game-name";
    public static final String CREATOR_NAME = "creator-name";
    private final String backendWebsocketUrl;
    private final TestContext testContext;

    public CreateGameDialog(Page page, String backendWebsocketUrl, TestContext testContext) {
        super(page, DIALOG);
        this.backendWebsocketUrl = backendWebsocketUrl;
        this.testContext = testContext;
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
        AtomicReference<String> logText = new AtomicReference<>();
        page.waitForConsoleMessage(new Page.WaitForConsoleMessageOptions().setPredicate(
                        consoleMessage -> {
                            String text = consoleMessage.text();
                            logText.set(text);
                            return text.startsWith("created game: ");
                        }),
                () -> this.clickButtonByTestId(VALIDATE));

//        page.onWebSocket(webSocket -> {
//            System.out.println("coucou"+webSocket.url());
//            webSocket.onFrameReceived(webSocketFrame -> {
//                System.out.println("toto\n"+webSocket.url());
//                System.out.println("toto\n"+webSocketFrame.text());
//            });
//        });
//        page.waitForWebSocket(new Page.WaitForWebSocketOptions().setPredicate(webSocket -> {
//                    String url = webSocket.url();
//                    webSocket.waitForFrameReceived(new WebSocket.WaitForFrameReceivedOptions().setPredicate(webSocketFrame -> {
//                                String text = webSocketFrame.text();
//                                return true;
//                            }),
//                            () -> {
//                            }
//                    );
//                    return true;
////            if (Objects.equals(backendWebsocketUrl, webSocket.url())) {
////                webSocket.waitForFrameReceived(new WebSocket.WaitForFrameReceivedOptions().setPredicate(webSocketFrame -> {
////                            String text = webSocketFrame.text();
////                            return false;
////                        }),
////                        () -> {
////                        }
////                );
////                return false;
////            } else {
////                return false;
////            }
//                }), () ->
//                        this.clickButtonByTestId(VALIDATE)
////                {}
//        );
        return Integer.parseInt(logText.get().split("created game: ")[1]);
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
