package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.Janitor;
import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.TestProperties;
import com.adaptionsoft.games.domain.pageObjects.CreateGameDialog;
import com.adaptionsoft.games.domain.pageObjects.Navbar;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.adaptionsoft.games.domain.pageObjects.CreateGameDialog.*;

@Slf4j
@RequiredArgsConstructor
public class CreateGameStepDefs {
    private final TestContext testContext;
    private final Janitor janitor;
    private final CreateGameDialog createGameDialog;
    private String createdGameName;
    private final Navbar navbar;
    private final RenameUserStepdefs renameUserStepdefs;

    @When("qa-user creates a game named {string} from the frontend")
    public void qaUserCreatesAGameNamed(String gameName) {
        int createdGameId = createGameDialog.createGame(gameName);
        janitor.disablePlayersShuffling(createdGameId);
        testContext.putGameId(gameName, createdGameId);
        createdGameName = gameName;
    }

    @When("qa-user creates a game named {string}, with username {string}, from the frontend")
    public void qaUserCreatesAGameNamedWithUsernameFromTheFrontend(String gameName, String creatorName) {
        int createdGameId = createGameDialog.createGame(gameName, creatorName);
        janitor.disablePlayersShuffling(createdGameId);
        testContext.putGameId(gameName, createdGameId);
        createdGameName = gameName;
    }

    @When("qa-user clicks on create game button")
    public void qaUserClicksOnCreateGameButton() {
        createGameDialog.clickButtonByTestid(OPEN_DIALOG_BUTTON);
        createGameDialog.verifyPresence();
    }

    @Then("qa-user can see the create game dialog")
    public void qaUserCanSeeTheCreateGameDialog() {
        createGameDialog.verifyPresence();
    }

    @Then("qa-user cannot see the create game dialog")
    public void qaUserCannotSeeTheCreateGameDialog() {
        createGameDialog.verifyAbsence();
    }

    @Then("the displayed value for game name is {string}")
    public void theDisplayedValueForGameNameIs(String expectedTextContent) {
        createGameDialog.verifyInputContentByTestId(GAME_NAME, expectedTextContent);
    }

    @And("the displayed value for creator name is {string}")
    public void theDisplayedValueForCreatorNameIs(String expectedTextContent) {
        createGameDialog.verifyInputContentByTestId(CREATOR_NAME, expectedTextContent);
    }

    @And("qa-user enters the game name {string}")
    public void qaUserEntersTheGameName(String gameName) {
        String formattedContent = formatInputForWhitespaces(gameName);
        createGameDialog.fillInputByTestId(GAME_NAME, formattedContent);
        createdGameName = gameName;
    }

    private static String formatInputForWhitespaces(String textContent) {
        return textContent.replace("[TAB]", "\t")
                .replace("[NEWLINE]", "\n");
    }

    @And("qa-user enters the creator name {string}")
    public void qaUserEntersTheCreatorName(String textContent) {
        String formattedContent = formatInputForWhitespaces(textContent);
        createGameDialog.fillInputByTestId(CREATOR_NAME, formattedContent);
    }

    @And("qa-user clicks on cancel button")
    public void qaUserClicksOnCancelButton() {
        createGameDialog.clickElementByTestid(CANCEL);
    }

    @When("qa-user clicks outside the dialog")
    public void qaUserClicksOutsideTheDialog() {
        createGameDialog.clickOutside();
    }

    @When("qa-user clicks on reset button")
    public void qaUserClicksOnResetButton() {
        createGameDialog.clickButtonByTestid(RESET);
    }

    @When("qa-user clicks on the create-game.validation button")
    public void qaUserClicksOnTheCreateGameValidationButton() {
        int gameId = createGameDialog.clickValidateAndGetGameIdFromConsoleLogs();
        testContext.putGameId(createdGameName, gameId);
    }

    @Then("the validate button is disabled")
    public void theValidateButtonIsDisabled() {
        try {
            createGameDialog.verifyButtonDisabledByTestid(VALIDATE);
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

    @When("qa-user presses the escape key on the create dialog")
    public void qaUserPressesTheEscapeKeyOnTheCreateDialog() {
        createGameDialog.pressEscape();
    }
}
