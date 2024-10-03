package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.Actor;
import com.adaptionsoft.games.domain.ActorService;
import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.TestProperties;
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

@RequiredArgsConstructor
@Slf4j
public class JoinGameStepDefs {
    private final Backend backend;
    private final TestContext testContext;
    private final ActorService actorService;
    private final GameRowActions gameRowActions;
    private final JoinGameDialog joinGameDialog;
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

    @When("qa-user clicks on the join button")
    public void qaUserClicksOnTheJoinButton() {
        gameRowActions.clickJoinButton(gameId1);
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
        joinGameDialog.clickButtonByTestid(VALIDATE);
    }

    @And("qa-user enters {string} in the join-game.player-name field")
    public void whenQaUserEntersInTheJoinGamePlayerNameField(String playerName) {
        joinGameDialog.fillInputByTestId(PLAYER_NAME, playerName);
    }

    @When("qa-user clicks on the join-game.reset button")
    public void qaUserClicksOnTheJoinGameResetButton() {
        joinGameDialog.clickButtonByTestid(JoinGameDialog.RESET);
    }

    @Then("qa-user cannot see the join game dialog")
    public void qaUserCannotSeeTheJoinGameDialog() {
        joinGameDialog.verifyAbsence();
    }

    @When("qa-user clicks on the join-game.cancel button")
    public void qaUserClicksOnTheJoinGameCancelButton() {
        joinGameDialog.clickButtonByTestid(JoinGameDialog.CANCEL);
    }
}
