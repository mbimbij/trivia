package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.TestProperties;
import com.adaptionsoft.games.domain.pageObjects.GameRowActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameDetailsStepDefs {
    private final TestContext testContext;
    private final Page page;
    private final TestProperties testProperties;
    private final GameRowActions gameDetailsPage;

    @And("i click on game details link for {string}")
    public void iClickOnGameDetailsLinkFor(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        String link = "/games/%d/details".formatted(gameId);
        Locator locator = page.getByTestId("game-details-%d".formatted(gameId));
        PlaywrightAssertions.assertThat(locator).hasAttribute("href", link);
        locator.click();
    }

    @When("i am on the on game details page for {string}")
    public void iAmOnTheOnGameDetailsPageFor(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        String url = (testProperties.getFrontendUrlBase() + "/games/%d/details").formatted(gameId);
        PlaywrightAssertions.assertThat(page).hasURL(url);
    }

    @And("qa-user refresh")
    public void iRefresh() {
        page.reload();
    }


    @When("\"qa-user\" joins {string} from the frontend")
    public void userJoinsGameFromTheFrontend(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        gameDetailsPage.join(gameId);
    }

    @When("i directly access the game-details page for {string}")
    public void iDirectlyAccessTheGameDetailsPageFor(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        String url = (testProperties.getFrontendUrlBase() + "/games/%d/details").formatted(gameId);
        page.navigate(url);
    }
}
