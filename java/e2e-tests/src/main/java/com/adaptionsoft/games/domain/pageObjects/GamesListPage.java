package com.adaptionsoft.games.domain.pageObjects;

import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.views.DisplayedGame;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.WebSocket;
import com.microsoft.playwright.WebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class GamesListPage extends PageWithStaticUrl {
    private final TestContext testContext;
    private final String backendWebsocketUrl;

    public GamesListPage(String basePath, Page page, TestContext testContext, String backendWebsocketUrl) {
        super(basePath + "/games", page);
        this.testContext = testContext;
        this.backendWebsocketUrl = backendWebsocketUrl;
    }

    /**
     * @param userNames Pass null or an empty collection to get games for all users
     * @return
     */
    public List<DisplayedGame> getDisplayedGamesForUsers(Collection<String> userNames) {
        Predicate<ElementHandle> predicate = (userNames == null || userNames.isEmpty())
                ? h -> true
                : h -> userNames.contains(h.querySelector(".creator-name").textContent().trim());
        return page.querySelectorAll(".game-row").stream()
                .filter(predicate)
                .map(this::convertToObject)
                .collect(Collectors.toList());
    }

    public DisplayedGame convertToObject(ElementHandle elementHandle) {
        return new DisplayedGame(
                elementHandle.querySelector(".name").textContent().trim(),
                elementHandle.querySelector(".creator-name").textContent().trim(),
                elementHandle.querySelector(".players-names").textContent().trim(),
                elementHandle.querySelector(".state").textContent().trim(),
                getButtonState(elementHandle, "start"),
                getButtonState(elementHandle, "join"),
                elementHandle.querySelector(".join").textContent().trim(),
                getButtonState(elementHandle, "goto"),
                getButtonState(elementHandle, "delete")
        );
    }

    public Boolean getButtonState(ElementHandle elementHandle, String buttonName) {
        return Optional.ofNullable(elementHandle.querySelector("." + buttonName + " button")).map(ElementHandle::isEnabled).orElse(null);
    }

    @SneakyThrows
    @Override
    public void navigateTo() {
        executeAndWaitForWebSocketMessages(() -> page.navigate(url));
        Thread.sleep(250);
    }

    public void executeAndWaitForWebSocketMessages(Runnable runnable) {
        Collection<String> expectedMessages = getExpectedWebSocketMessages();
        log.info("Navigating to %s".formatted(url));
        page.waitForWebSocket(new Page.WaitForWebSocketOptions().setPredicate(webSocket -> {
                    // other websocket communications occur
                    if (isBackendWebsocket(webSocket)) {
                        waitUntilExpectedWebsocketFramesSent(webSocket, expectedMessages);
                        return true;
                    } else {
                        return false;
                    }
                }),
                runnable);
    }

    private boolean isBackendWebsocket(WebSocket webSocket) {
        return Objects.equals(backendWebsocketUrl, webSocket.url());
    }

    private static void waitUntilExpectedWebsocketFramesSent(WebSocket webSocket, Collection<String> expectedMessages) {
        webSocket.waitForFrameSent(new WebSocket.WaitForFrameSentOptions().setPredicate(webSocketFrame -> {
            String text = webSocketFrame.text();
            expectedMessages.removeIf(text::contains);
            return expectedMessages.isEmpty();
        }), () -> {
        });
    }

    private Collection<String> getExpectedWebSocketMessages() {
        Set<String> messages = new HashSet<>();
        messages.add("destination:/topic/games/created");
        messages.add("destination:/topic/games/deleted");
        testContext.listGameIds()
                .stream()
                .map("destination:/topic/games/%d"::formatted)
                .forEach(messages::add);
        return messages;
    }
}
