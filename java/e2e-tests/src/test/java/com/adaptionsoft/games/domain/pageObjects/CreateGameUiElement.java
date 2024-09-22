package com.adaptionsoft.games.domain.pageObjects;

import com.adaptionsoft.games.domain.TestContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateGameUiElement extends UiElementObject {

    public CreateGameUiElement(Page page, TestContext testContext) {
        super(page);
    }

    // TODO ajouter un test de création de partie depuis le frontend
    @SneakyThrows
    public int createGame(String gameName) {
        PlaywrightAssertions.assertThat(page.getByTestId("create-game-dialog")).not().isAttached();
        page.getByTestId("create-game").click();
        PlaywrightAssertions.assertThat(page.getByTestId("create-game-dialog")).isAttached();

        page.getByTestId("create-game-name").fill(gameName);

        AtomicReference<String> logText = new AtomicReference<>();
        page.waitForConsoleMessage(new Page.WaitForConsoleMessageOptions().setPredicate(
                        consoleMessage -> {
                            String text = consoleMessage.text();
                            logText.set(text);
                            return text.startsWith("created game: ");
                        }),
                page.getByTestId("validate")::click);

        PlaywrightAssertions.assertThat(page.getByTestId("create-game-dialog")).not().isAttached();

        return Integer.parseInt(logText.get().split("created game: ")[1]);
    }
}
