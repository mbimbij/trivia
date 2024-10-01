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

@RequiredArgsConstructor
@Slf4j
public class JoinGameStepDefs {
    private final Backend backend;
    private final TestContext testContext;
    private final ActorService actorService;
    private final GameRowActions gameRowActions;
    private final JoinGameDialog joinGameDialog;
    private @NotBlank Integer gameId;

    @Given("an already existing game")
    public void anAlreadyExistingGameCreatedBy() {
        Actor testUser1 = actorService.getActorByLookupName(TestProperties.TEST_USER_NAME_1);
        GameResponseDto gameResponseDto = backend.createGame(TestProperties.CREATED_GAME_NAME, testUser1.toUserDto());
        gameId = gameResponseDto.id();
        testContext.putGameId(TestProperties.CREATED_GAME_NAME, gameId);
    }

    @When("qa-user clicks on the join button")
    public void qaUserClicksOnTheJoinButton() {
        gameRowActions.clickJoinButton(gameId);
    }

    @Then("qa-user can see the join game dialog")
    public void qaUserCanSeeTheJoinGameDialog() {
        joinGameDialog.verifyIsPresent();
    }

    @And("the displayed value for player name is {string}")
    public void theDisplayedValueForPlayerNameIs(String expectedPlayerName) {
        joinGameDialog.verifyInputContentByTestId(PLAYER_NAME, expectedPlayerName);
    }
}
