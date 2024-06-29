package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.TestProperties;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OngoingGameStepsDefs {

    private final TestContext gameService;

    private final TestProperties testProperties;

    @When("i am on the on game page for {string}")
    public void iAmOnTheOnGamePageFor(String gameName) {
        Integer gameId = gameService.getGameByName(gameName).id();
        String url = (testProperties.getFrontendUrlBase() + "/games/%d").formatted(gameId);
        PlaywrightAssertions.assertThat(StepsDefs.getPage()).hasURL(url);
    }
}
