package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.Janitor;
import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.pageObjects.CreateGameUiElement;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreateGameStepDefs {
    private final TestContext testContext;
    private final Janitor janitor;
    private final CreateGameUiElement createGameUiElement;

    @When("qa-user creates a game named {string} from the frontend")
    public void qaUserCreatesAGameNamed(String gameName) {
        int createdGameId = createGameUiElement.createGame(gameName);
        janitor.disablePlayersShuffling(createdGameId);
        testContext.putGameId(gameName, createdGameId);
    }

    @When("qa-user creates a game named {string}, with username {string}, from the frontend")
    public void qaUserCreatesAGameNamedWithUsernameFromTheFrontend(String gameName, String creatorName) {
        int createdGameId = createGameUiElement.createGame(gameName, creatorName);
        janitor.disablePlayersShuffling(createdGameId);
        testContext.putGameId(gameName, createdGameId);
    }

    @When("qa-user clics on create game button")
    public void qaUserClicsOnCreateGameButton() {
        createGameUiElement.clickButtonByTestid(CreateGameUiElement.OPEN_DIALOG_BUTTON);
    }

    @Then("qa-user can see the create game dialog")
    public void qaUserCanSeeTheCreateGameDialog() {
        createGameUiElement.verifyExistenceByTestId(CreateGameUiElement.DIALOG);
    }

    @Then("qa-user cannot see the create game dialog")
    public void qaUserCannotSeeTheCreateGameDialog() {
        createGameUiElement.verifyAbsenceByTestId(CreateGameUiElement.DIALOG);
    }

    @Then("the displayed value for game name is {string}")
    public void theDisplayedValueForGameNameIs(String expectedTextContent) {
        createGameUiElement.verifyInputContentByTestId(CreateGameUiElement.GAME_NAME, expectedTextContent);
    }

    @And("the displayed value for creator name is {string}")
    public void theDisplayedValueForCreatorNameIs(String expectedTextContent) {
        createGameUiElement.verifyInputContentByTestId(CreateGameUiElement.CREATOR_NAME, expectedTextContent);
    }

    @And("qa-user enters the game name {string}")
    public void qaUserEntersTheGameName(String textContent) {
        createGameUiElement.fillInputByTestId(CreateGameUiElement.GAME_NAME, textContent);
    }

    @And("qa-user enters the creator name {string}")
    public void qaUserEntersTheCreatorName(String textContent) {
        createGameUiElement.fillInputByTestId(CreateGameUiElement.CREATOR_NAME, textContent);
    }

    @And("qa-user clicks on cancel button")
    public void qaUserClicksOnCancelButton() {
        createGameUiElement.clickElementByTestid(CreateGameUiElement.CANCEL);
    }

    @When("qa-user clicks outside the dialog")
    public void qaUserClicksOutsideTheDialog() {
        createGameUiElement.clickOutsideDialog();
    }

    @When("qa-user clicks on reset button")
    public void qaUserClicksOnResetButton() {
        createGameUiElement.clickButtonByTestid(CreateGameUiElement.RESET);
    }

    @When("qa-user clicks on the create-game.validation button")
    public void qaUserClicksOnTheCreateGameValidationButton() {
        createGameUiElement.clickButtonByTestid(CreateGameUiElement.VALIDATE);
    }
}
