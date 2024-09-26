package com.adaptionsoft.games.domain.pageObjects;

import com.adaptionsoft.games.domain.TestContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.BoundingBox;
import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateGameUiElement extends UiElementObject {
    public static final String OPEN_DIALOG_BUTTON = "create-game";
    public static final String DIALOG = "create-game-dialog";
    public static final String GAME_NAME = "game-name";
    public static final String CREATOR_NAME = "creator-name";
    public static final String CANCEL = "cancel";
    public static final String RESET = "reset";
    public static final String VALIDATE = "validate";

    public CreateGameUiElement(Page page, TestContext testContext) {
        super(page);
    }

    // TODO ajouter un test de création de partie depuis le frontend
    @SneakyThrows
    public int createGame(String gameName) {
        verifyAbsenceByTestId(DIALOG);
        clickButtonByTestid(OPEN_DIALOG_BUTTON);
        verifyPresenceByTestId(DIALOG);
        fillInputByTestId(GAME_NAME, gameName);
        int newGameId = clickValidateAndGetGameIdFromConsoleLogs();
        verifyAbsenceByTestId(DIALOG);
        return newGameId;
    }

    private int clickValidateAndGetGameIdFromConsoleLogs() {
        AtomicReference<String> logText = new AtomicReference<>();
        page.waitForConsoleMessage(new Page.WaitForConsoleMessageOptions().setPredicate(
                        consoleMessage -> {
                            String text = consoleMessage.text();
                            logText.set(text);
                            return text.startsWith("created game: ");
                        }),
                () -> this.clickButtonByTestid(VALIDATE));
        return Integer.parseInt(logText.get().split("created game: ")[1]);
    }

    // TODO ajouter un test de création de partie depuis le frontend
    @SneakyThrows
    public int createGame(String gameName, String creatorName) {
        PlaywrightAssertions.assertThat(page.getByTestId("create-game-dialog")).not().isAttached();
        page.getByTestId("create-game").click();
        PlaywrightAssertions.assertThat(page.getByTestId("create-game-dialog")).isAttached();

        page.getByTestId("game-name").fill(gameName);
        page.getByTestId("creator-name").fill(creatorName);

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

    public void clickOutsideDialog() {
        Locator locator = page.getByTestId(VALIDATE);
        BoundingBox boundingBox = locator.boundingBox();
        // TODO être un peu plus malin sur ce test, que ça soit robuste à la taille de l'écran et que ça ne puisse pas sortir
        double xPosition = boundingBox.x + boundingBox.width + 20;
        double yPosition = boundingBox.y;
        page.mouse().click(xPosition,yPosition);
    }
}
