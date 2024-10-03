package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;
import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicReference;

public class CreateGameDialog extends Dialog {
    public static final String OPEN_DIALOG_BUTTON = "create-game";
    public static final String DIALOG = "create-game-dialog";
    public static final String GAME_NAME = "game-name";
    public static final String CREATOR_NAME = "creator-name";
    public static final String CANCEL = "cancel";
    public static final String RESET = "reset";
    public static final String VALIDATE = "validate";

    public CreateGameDialog(Page page) {
        super(page, DIALOG);
    }

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

    public int clickValidateAndGetGameIdFromConsoleLogs() {
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
        verifyAbsenceByTestId(DIALOG);
        clickButtonByTestid(OPEN_DIALOG_BUTTON);
        verifyPresenceByTestId(DIALOG);
        fillInputByTestId(GAME_NAME, gameName);
        fillInputByTestId(CREATOR_NAME, creatorName);
        int newGameId = clickValidateAndGetGameIdFromConsoleLogs();
        verifyAbsenceByTestId(DIALOG);
        return newGameId;
    }

}
