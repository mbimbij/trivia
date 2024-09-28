package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.Janitor;
import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.TestProperties;
import com.adaptionsoft.games.domain.pageObjects.CreateGameUiElement;
import com.adaptionsoft.games.domain.pageObjects.Navbar;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.adaptionsoft.games.domain.pageObjects.CreateGameUiElement.*;

@Slf4j
@RequiredArgsConstructor
public class CreateGameStepDefs {
    private final TestContext testContext;
    private final Janitor janitor;
    private final CreateGameUiElement createGameUiElement;
    private String createdGameName;
    private final Navbar navbar;
    private final RenameUserStepdefs renameUserStepdefs;

    @When("qa-user creates a game named {string} from the frontend")
    public void qaUserCreatesAGameNamed(String gameName) {
        int createdGameId = createGameUiElement.createGame(gameName);
        janitor.disablePlayersShuffling(createdGameId);
        testContext.putGameId(gameName, createdGameId);
        createdGameName = gameName;
    }

    @When("qa-user creates a game named {string}, with username {string}, from the frontend")
    public void qaUserCreatesAGameNamedWithUsernameFromTheFrontend(String gameName, String creatorName) {
        int createdGameId = createGameUiElement.createGame(gameName, creatorName);
        janitor.disablePlayersShuffling(createdGameId);
        testContext.putGameId(gameName, createdGameId);
        createdGameName = gameName;
    }

    @When("qa-user clicks on create game button")
    public void qaUserClicksOnCreateGameButton() {
        createGameUiElement.clickButtonByTestid(OPEN_DIALOG_BUTTON);
        createGameUiElement.waitForDialogToOpen();
    }

    @Then("qa-user can see the create game dialog")
    public void qaUserCanSeeTheCreateGameDialog() {
        createGameUiElement.verifyPresenceByTestId(DIALOG);
    }

    @Then("qa-user cannot see the create game dialog")
    public void qaUserCannotSeeTheCreateGameDialog() {
        createGameUiElement.verifyAbsenceByTestId(DIALOG);
    }

    @Then("the displayed value for game name is {string}")
    public void theDisplayedValueForGameNameIs(String expectedTextContent) {
        createGameUiElement.verifyInputContentByTestId(GAME_NAME, expectedTextContent);
    }

    @And("the displayed value for creator name is {string}")
    public void theDisplayedValueForCreatorNameIs(String expectedTextContent) {
        createGameUiElement.verifyInputContentByTestId(CREATOR_NAME, expectedTextContent);
    }

    @And("qa-user enters the game name {string}")
    public void qaUserEntersTheGameName(String gameName) {
        String formattedContent = formatInputForWhitespaces(gameName);
        createGameUiElement.fillInputByTestId(GAME_NAME, formattedContent);
        createdGameName = gameName;
    }

    private static String formatInputForWhitespaces(String textContent) {
        return textContent.replace("[TAB]", "\t")
                .replace("[NEWLINE]", "\n");
    }

    @And("qa-user enters the creator name {string}")
    public void qaUserEntersTheCreatorName(String textContent) {
        String formattedContent = formatInputForWhitespaces(textContent);
        createGameUiElement.fillInputByTestId(CREATOR_NAME, formattedContent);
    }

    @And("qa-user clicks on cancel button")
    public void qaUserClicksOnCancelButton() {
        createGameUiElement.clickElementByTestid(CANCEL);
    }

    @When("qa-user clicks outside the dialog")
    public void qaUserClicksOutsideTheDialog() {
        createGameUiElement.clickOutsideDialog();
    }

    @When("qa-user clicks on reset button")
    public void qaUserClicksOnResetButton() {
        createGameUiElement.clickButtonByTestid(RESET);
    }

    @When("qa-user clicks on the create-game.validation button")
    public void qaUserClicksOnTheCreateGameValidationButton() {
        int gameId = createGameUiElement.clickValidateAndGetGameIdFromConsoleLogs();
        testContext.putGameId(createdGameName, gameId);
    }

    @Then("the validate button is disabled")
    public void theValidateButtonIsDisabled() {
        try {
            createGameUiElement.verifyButtonDisabledByTestid(VALIDATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @And("qa-user name was not changed")
    public void qaUserNameWasNotChanged() {
        if(!Objects.equals(navbar.getDisplayedUserName(), TestProperties.QA_FRONTEND_USER_NAME)){
            renameUserStepdefs.renameQaUser(TestProperties.QA_FRONTEND_USER_NAME);
        }
    }
}
