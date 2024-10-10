package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;

public class Navbar extends UiElementObject {
    public static final String NAVBAR_USERNAME_TESTID = "navbar-username";

    public Navbar(Page page) {
        super(page);
    }

    public String getDisplayedUserName() {
        return getTextContentByTestid(NAVBAR_USERNAME_TESTID);
    }

    public void verifyDisplayedUserName(String expectedUserName) {
        verifyTextContentByTestId(NAVBAR_USERNAME_TESTID, expectedUserName);
    }
}
