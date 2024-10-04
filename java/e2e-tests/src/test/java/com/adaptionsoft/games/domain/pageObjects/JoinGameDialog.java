package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;

public class JoinGameDialog extends Dialog {
    public static final String DIALOG = "join-game-dialog";
    public static final String PLAYER_NAME = "player-name";
    public static final String CANCEL = "cancel";
    public static final String RESET = "reset";
    public static final String VALIDATE = "validate";

    public JoinGameDialog(Page page) {
        super(page, DIALOG);
    }

    public void verifyValidateButtonDisabled() {
        verifyButtonDisabledByTestId(VALIDATE);
    }
}
