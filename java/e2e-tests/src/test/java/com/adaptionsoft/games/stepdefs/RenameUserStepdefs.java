package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.TestProperties;
import com.adaptionsoft.games.domain.pageObjects.GamesListPage;
import com.adaptionsoft.games.domain.pageObjects.Navbar;
import com.adaptionsoft.games.domain.pageObjects.RenameUser;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class RenameUserStepdefs {
    private final GamesListPage gamesListPage;
    private final RenameUser renameUser;
    private final Navbar navbar;
    private final TestContext testContext;

    @When("qa-user changes his name to {string}")
    public void renameQaUser(String newName) {
        gamesListPage.navigateTo();
        renameUser.renameUser(newName);
        testContext.setUserRenamed(!Objects.equals(navbar.getDisplayedUserName(), TestProperties.QA_FRONTEND_USER_NAME));
        navbar.verifyDisplayedUserName(newName);
    }
}
