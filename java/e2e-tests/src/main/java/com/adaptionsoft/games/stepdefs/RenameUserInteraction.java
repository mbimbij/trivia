package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.pageObjects.Navbar;
import com.microsoft.playwright.Page;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RenameUserInteraction {
    private final Navbar navbar;
    private final Page page;

    @When("qa-user changes his name to {string}")
    public void renameQaUser(String newName) {
        String command = "window.renameUser('%s')".formatted(newName);
        page.evaluate(command);
        navbar.verifyDisplayedUserName(newName);
    }
}
