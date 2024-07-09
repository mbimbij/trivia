package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateGameUiElement extends UiElementObject {
    private final Console console;
    public CreateGameUiElement(Page page, Console console) {
        super(page);
        this.console = console;
    }

    // TODO ajouter un test de création de partie depuis le frontend
    public void createGame(String gameName) {
        page.getByTestId("create-game-name").fill(gameName);
        Locator button = page.getByTestId("create-game-validate");
        PlaywrightAssertions.assertThat(button).isVisible();
        PlaywrightAssertions.assertThat(button).isEnabled();
        button.click();
        page.evaluate("console.log('coucou')");
        page.waitForConsoleMessage(new Page.WaitForConsoleMessageOptions().setPredicate(consoleMessage -> {
            return consoleMessage.text().matches("created game:");
        }),() -> {});
        System.out.println();
//        Awaitility.await()
//                .atMost(TestUtils.maxWaitDuration)
//                .pollInterval(TestUtils.pollInterval)
//                .until(() -> !console.findLogsMatching("created game:").isEmpty());
//        List<String> gameCreatedLogs = console.findLogsMatching("created game:");
//        assertThat(gameCreatedLogs).hasSize(1);
//        String first = gameCreatedLogs.getFirst();
//        System.out.println();
    }
}
