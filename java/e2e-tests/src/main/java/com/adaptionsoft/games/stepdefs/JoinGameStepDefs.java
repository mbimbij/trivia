package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.*;
import com.adaptionsoft.games.domain.pageObjects.Backend;
import com.adaptionsoft.games.domain.pageObjects.GameRowActions;
import com.adaptionsoft.games.domain.pageObjects.JoinGameDialog;
import com.adaptionsoft.games.trivia.game.web.GameResponseDto;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.adaptionsoft.games.domain.pageObjects.JoinGameDialog.PLAYER_NAME;
import static com.adaptionsoft.games.domain.pageObjects.JoinGameDialog.VALIDATE;
import static com.adaptionsoft.games.utils.TestUtils.formatInputForWhitespaces;

@RequiredArgsConstructor
@Slf4j
public class JoinGameStepDefs {
    private final Backend backend;
    private final TestContext testContext;
    private final ActorService actorService;
    private final GameRowActions gameRowActions;
    private final JoinGameDialog joinGameDialog;
    private final Janitor janitor;
    private @NotBlank Integer gameId1;
    private @NotBlank Integer gameId2;

    @Given("an already existing game")
    public void anAlreadyExistingGameCreatedBy() {
        Actor testUser1 = actorService.getActorByLookupName(TestProperties.TEST_USER_NAME_1);
        GameResponseDto gameResponseDto1 = backend.createGame(TestProperties.TEST_GAME_NAME_1, testUser1.toUserDto());
        gameId1 = gameResponseDto1.id();
        testContext.putGameId(TestProperties.TEST_GAME_NAME_1, gameId1);

        Actor testUser2 = actorService.getActorByLookupName(TestProperties.TEST_USER_NAME_2);
        GameResponseDto gameResponseDto2 = backend.createGame(TestProperties.TEST_GAME_NAME_2, testUser2.toUserDto());
        gameId2 = gameResponseDto2.id();
        testContext.putGameId(TestProperties.TEST_GAME_NAME_2, gameId2);
    }


    @When("qa-user joins {string} from the frontend")
    public void userJoinsGameFromTheFrontend(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        gameRowActions.clickJoinButton(gameId);
        joinGameDialog.verifyPresence();
        joinGameDialog.clickValidate();
        joinGameDialog.verifyAbsence();
    }

    @When("qa-user clicks on the join button")
    public void qaUserClicksOnTheJoinButton() {
        gameRowActions.clickJoinButton(gameId1);
        joinGameDialog.verifyPresence();
    }

    @And("qa-user clicks on the join button for the other game")
    public void qaUserClicksOnTheJoinButtonForTheOtherGame() {
        gameRowActions.clickJoinButton(gameId2);
        joinGameDialog.verifyPresence();
    }

    @Then("qa-user can see the join game dialog")
    public void qaUserCanSeeTheJoinGameDialog() {
        joinGameDialog.verifyPresence();
    }

    @And("the displayed value for player name is {string}")
    public void theDisplayedValueForPlayerNameIs(String expectedPlayerName) {
        joinGameDialog.verifyPresence();
        joinGameDialog.verifyInputContentByTestId(PLAYER_NAME, expectedPlayerName);
    }

    @And("qa-user clicks on the join-game.validation button")
    public void qaUserClicksOnTheJoinGameValidationButton() {
        joinGameDialog.clickButtonByTestId(VALIDATE);
    }

    @And("qa-user enters {string} in the join-game.player-name field")
    public void whenQaUserEntersInTheJoinGamePlayerNameField(String playerName) {
        String formattedContent = formatInputForWhitespaces(playerName);
        joinGameDialog.fillInputByTestId(PLAYER_NAME, formattedContent);
    }

    @When("qa-user clicks on the join-game.reset button")
    public void qaUserClicksOnTheJoinGameResetButton() {
        joinGameDialog.clickButtonByTestId(JoinGameDialog.RESET);
    }

    @Then("qa-user cannot see the join game dialog")
    public void qaUserCannotSeeTheJoinGameDialog() {
        joinGameDialog.verifyAbsence();
    }

    @When("qa-user clicks on the join-game.cancel button")
    public void qaUserClicksOnTheJoinGameCancelButton() {
        joinGameDialog.clickButtonByTestId(JoinGameDialog.CANCEL);
        joinGameDialog.verifyAbsence();
    }

    @And("qa-user clicks outside the join dialog")
    public void qaUserClicksOutsideTheJoinDialog() {
        joinGameDialog.clickOutside();
    }

    @When("qa-user presses the escape key on the join dialog")
    public void qaUserPressesTheEscapeKeyOnTheJoinDialog() {
        joinGameDialog.pressEscape();
    }

    @Then("the join-game.validate button is disabled")
    public void theJoinGameValidateButtonIsDisabled() {
        joinGameDialog.verifyValidateButtonDisabled();
    }

    @Given("an exception is thrown when calling joinGame")
    public void anExceptionIsThrownWhenCallingJoinGame() {
        janitor.throwExceptionWhenCallJoin();
    }

    @And("qa-user can see the join-game.backend-error-message")
    public void qaUserCanSeeTheJoinGameBackendErrorMessage() {
        joinGameDialog.verifyBackendErrorMessagePresent();
    }
}
