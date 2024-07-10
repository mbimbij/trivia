package com.adaptionsoft.games.domain.pageObjects;

import com.adaptionsoft.games.domain.TestContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateGameUiElement extends UiElementObject {
    private final Console console;
    private final TestContext testContext;

    public CreateGameUiElement(Page page, Console console, TestContext testContext) {
        super(page);
        this.console = console;
        this.testContext = testContext;
    }

    // TODO ajouter un test de création de partie depuis le frontend
    @SneakyThrows
    public int createGame(String gameName) {
        page.getByTestId("create-game-name").fill(gameName);
        Locator button = page.getByTestId("create-game-validate");
        PlaywrightAssertions.assertThat(button).isVisible();
        PlaywrightAssertions.assertThat(button).isEnabled();

        AtomicReference<String> logText = new AtomicReference<>();
        page.waitForConsoleMessage(new Page.WaitForConsoleMessageOptions().setPredicate(
                        consoleMessage -> {
                            String text = consoleMessage.text();
                            logText.set(text);
                            return text.startsWith("created game: ");
                        }),
                button::click);

        return Integer.parseInt(logText.get().split("created game: ")[1]);
    }
}
