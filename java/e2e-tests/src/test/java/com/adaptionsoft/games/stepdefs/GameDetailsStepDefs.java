package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.pageObjects.GameDetailsPage;
import com.adaptionsoft.games.domain.pageObjects.GameRowActions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class GameDetailsStepDefs {
    private final TestContext testContext;
    private final GameRowActions gameRowActions;
    private final GameDetailsPage gameDetailsPage;

    @And("qa-user clicks on game details link for {string}")
    public void iClickOnGameDetailsLinkFor(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);

        assertThat(gameRowActions.getGameDetailsLink(gameId))
                .isEqualTo("/games/%d/details".formatted(gameId));

        gameRowActions.clickGameDetailsLink(gameId);
    }

    @When("i am on the on game details page for {string}")
    public void iAmOnTheOnGameDetailsPageFor(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        gameDetailsPage.waitForUrl(gameId);
    }

    @When("qa-user joins {string} from the frontend")
    public void userJoinsGameFromTheFrontend(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        gameRowActions.join(gameId);
    }

    @When("qa-user directly access the game-details page for {string}")
    public void iDirectlyAccessTheGameDetailsPageFor(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        gameDetailsPage.navigateTo(gameId);
    }

    @When("qa-user directly access the game-details page for game id = {int}")
    public void iDirectlyAccessTheGameDetailsPageForGameId(int gameId) {
        gameDetailsPage.navigateTo(gameId);
    }
}
