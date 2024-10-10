package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;

public class JoinGameDialog extends Dialog {
    public static final String DIALOG = "join-game-dialog";
    public static final String PLAYER_NAME = "player-name";

    public JoinGameDialog(Page page) {
        super(page, DIALOG);
    }
}
