package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.BackendActor;
import com.adaptionsoft.games.domain.FrontendActor;
import com.adaptionsoft.games.domain.TestActor;
import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.views.DisplayedGame;
import com.adaptionsoft.games.trivia.web.CreateGameRequestDto;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import com.adaptionsoft.games.utils.TestUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
public class GameListStepDefs {

    @Getter
    private final RestTemplate restTemplate = new RestTemplate();

    private final Page page;
    private final FrontendActor qaFrontendActor;
    private final BackendActor qaBackendActor;
    private final BackendActor backendActor1;
    private final BackendActor backendActor2;

    private final TestContext testContext;
    private final Map<String, TestActor> actorsByName = new HashMap<>();

    private GameResponseDto game1;
    private GameResponseDto game2;
    private GameResponseDto createdGame;

    @Value("${test.frontend-url-base}")
    private String frontendUrlBase;
    @Getter
    @Value("${test.backend-url-base}")
    private String backendUrlBase;

    public GameListStepDefs(Page page,
                            FrontendActor qaFrontendActor,
                            @Qualifier("qaBackendActor") BackendActor qaBackendActor,
                            @Qualifier("backendActor1") BackendActor backendActor1,
                            @Qualifier("backendActor2") BackendActor backendActor2,
                            TestContext testContext) {
        this.page = page;
        this.qaFrontendActor = qaFrontendActor;
        this.qaBackendActor = qaBackendActor;
        this.backendActor1 = backendActor1;
        this.backendActor2 = backendActor2;
        this.testContext = testContext;
    }

    @PostConstruct
    void postConstruct() {
        actorsByName.put(TestContext.QA_USER_NAME, qaFrontendActor);
        actorsByName.put(TestContext.TEST_USER_NAME_1, backendActor1);
        actorsByName.put(TestContext.TEST_USER_NAME_2, backendActor2);
    }


    @Before
    public void setUp() {
        qaFrontendActor.clearConsoleLogs();
    }

    @After
    public void tearDown() {
        deleteTestGames();
    }

    private void deleteTestGames() {
        restTemplate.delete(backendUrlBase + "/games/tests");
        Optional.ofNullable(this.game1).map(GameResponseDto::name).ifPresent(testContext::deleteGame);
        Optional.ofNullable(this.game2).map(GameResponseDto::name).ifPresent(testContext::deleteGame);
        Optional.ofNullable(this.createdGame).map(GameResponseDto::name).ifPresent(testContext::deleteGame);
    }

    @Given("a logged-in test user on the game-list page")
    public void logged_in_test_user_on_game_list_page() {
        if(!qaFrontendActor.isLoggedIn()){
            qaFrontendActor.login();
        } else {
            qaFrontendActor.gotoGamesPageByUrl();
        }
    }

    @Given("2 existing games")
    public void games() {
        String gameName1 = "test-game-1";
        game1 = createGame(gameName1, backendActor1.toUserDto());
        testContext.putGameId(gameName1, game1.id());
        String gameName2 = "test-game-2";
        game2 = createGame(gameName2, qaBackendActor.toUserDto());
        testContext.putGameId(gameName2, game2.id());
    }

    private GameResponseDto createGame(String gameName, UserDto user) {
        CreateGameRequestDto requestDto = new CreateGameRequestDto(gameName, user);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games",
                requestDto,
                GameResponseDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return responseEntity.getBody();
    }

    @Then("qa-user sees the following games, filtered for creators \"{strings}\"")
    public void theFollowingGamesAreDisplayedForUsers(Collection<String> userNames, Collection<DisplayedGame> expected) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(TestUtils.pollInterval)
                .untilAsserted(
                        () -> {
                            List<DisplayedGame> actual = qaFrontendActor.getDisplayedGamesForUsers(userNames);
                            assertThat(actual).isEqualTo(expected);
                        }
                );
    }

    @Then("the following games are displayed")
    public void theFollowingGamesAreDisplayed(Collection<DisplayedGame> expected) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(TestUtils.pollInterval)
                .untilAsserted(
                        () -> assertThat(qaFrontendActor.getDisplayedGamesForUsers(emptyList())).isEqualTo(expected)
                );
    }

    public void userJoinsGameFromBackend(String userName, String gameName) {
        UserDto user = testContext.getUserDtoByName(userName);
        int gameId = testContext.getGameIdForName(gameName);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games/{gameId}/players/{userId}/join",
                new UserDto(user.id(), user.name()),
                GameResponseDto.class,
                gameId,
                user.id());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @When("{string} joins {string}")
    public void userJoinsGame(String userName, String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        TestActor testActor = getActorByName(userName);
        testActor.join(gameId);
    }

    private TestActor getActorByName(String userName) {
        assertThat(actorsByName).containsKey(userName);
        return actorsByName.get(userName);
    }

    public void userStartsGameFromBackend(String userName, String gameName) {
        UserDto user = testContext.getUserDtoByName(userName);
        int game = testContext.getGameIdForName(gameName);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games/{gameId}/players/{userId}/start",
                new UserDto(user.id(), user.name()),
                GameResponseDto.class,
                game,
                user.id());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @When("{string} starts {string}")
    public void testUserStartsTestGame(String userName, String gameName) {
        if (Objects.equals(testContext.getQaUser().name(), userName)) {
            qaUserClicksOnButtonForGame("start", gameName);
        } else {
            userStartsGameFromBackend(userName, gameName);
        }
    }

    @When("{string} creates a game named {string}")
    public void testUserCreatesAGameNamed(String userName, String gameName) {
        UserDto creator = testContext.getUserDtoByName(userName);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games",
                new CreateGameRequestDto(gameName, creator),
                GameResponseDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        createdGame = responseEntity.getBody();
        testContext.putGameId(gameName, createdGame.id());
    }

    @When("qa-user creates a game named {string}")
    public void qaUserCreatesAGameNamed(String gameName) {
        qaBackendActor.createGame(gameName);
    }

    @When("{string} deletes the game named {string}")
    public void deletesTheGameNamed(String userName, String gameName) {
        UserDto creator = testContext.getUserDtoByName(userName);
        int gameId = testContext.getGameIdForName(gameName);
        restTemplate.delete(backendUrlBase + "/games/{gameId}", gameId);
        testContext.deleteGame(gameName);
    }

    @When("qa-user clicks on {string} button for {string}")
    public void qaUserClicksOnButtonForGame(String buttonName, String gameName) {
        qaFrontendActor.clickOnButtonForGame_GameList(buttonName, gameName);
    }

    @Given("previous test data cleared")
    public void previousTestDataCleared() {
        deleteTestGames();
    }

    @And("no error is displayed in the console")
    public void noErrorIsDisplayedInTheConsole() {
        List<String> errorLogs = qaFrontendActor.getErrorLogs();
        String errorLogsString = String.join("\n", errorLogs);
        String failMessage = "The following error logs were present%n%s".formatted(errorLogsString);
        assertThat(errorLogs)
                .withFailMessage(() -> failMessage)
                .isEmpty();
    }

    @And("i click on game details link for {string}")
    public void noErrorIsDisplayedInTheConsole(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        String link = "/games/%d/details".formatted(gameId);
        Locator locator = page.getByTestId("game-details-%d".formatted(gameId));
        PlaywrightAssertions.assertThat(locator).hasAttribute("href", link);
        locator.click();
    }

    @When("i am on the on game details page for {string}")
    public void iAmOnTheOnGameDetailsPageFor(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        String url = (frontendUrlBase + "/games/%d/details").formatted(gameId);
        PlaywrightAssertions.assertThat(page).hasURL(url);
    }


    @When("i directly access the game-details page for {string}")
    public void iDirectlyAccessTheGameDetailsPageFor(String gameName) {
        int gameId = testContext.getGameIdForName(gameName);
        String url = (frontendUrlBase + "/games/%d/details").formatted(gameId);
        page.navigate(url);
    }

    @And("i refresh")
    public void iRefresh() {
        page.reload();
    }

    @Given("\"test-game-2\" started")
    public void started() {
        // TODO insert a started game in database directly
        backendActor1.join(game2.id());
        qaBackendActor.start(game2);
    }

    @ParameterType("(is|is not)")
    public IS_OR_NOT isOrNot(String stringValue) {
        return IS_OR_NOT.fromString(stringValue);
    }

}
