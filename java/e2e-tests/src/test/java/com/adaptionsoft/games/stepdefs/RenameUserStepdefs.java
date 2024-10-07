package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.pageObjects.GamesListPage;
import com.adaptionsoft.games.domain.pageObjects.Navbar;
import com.adaptionsoft.games.domain.pageObjects.RenameUser;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RenameUserStepdefs {
    private final GamesListPage gamesListPage;
    private final RenameUser renameUser;
    private final Navbar navbar;

    @When("qa-user changes his name to {string}")
    public void renameQaUser(String newName) {
        gamesListPage.navigateTo();
        renameUser.renameUser(newName);
        navbar.verifyDisplayedUserName(newName);
    }

    public void renameQaUserWithoutNavigation(String newName) {
        renameUser.renameUser(newName);
        navbar.verifyDisplayedUserName(newName);
    }
}
