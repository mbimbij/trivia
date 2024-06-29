package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.*;
import com.adaptionsoft.games.domain.views.DisplayedGame;
import com.adaptionsoft.games.trivia.web.CreateGameRequestDto;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import com.adaptionsoft.games.utils.TestUtils;
import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.WaitUntilState;
import io.cucumber.java.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@RequiredArgsConstructor
public class StepDefs {

    private static final Logger log = LoggerFactory.getLogger(StepDefs.class);

    @Getter
    private final RestTemplate restTemplate = new RestTemplate();

    private final String userName1 = "test-user-1";
    private final String userName2 = "test-user-2";
    private final UserDto user1 = new UserDto("id-test-user-1", userName1);
    private final UserDto user2 = new UserDto("id-test-user-2", userName2);

    public final FrontendActor qaFrontendActor;
    private final Page page;
    private BackendActor qaBackendActor;
    private BackendActor backendActor1;
    private BackendActor backendActor2;

    private final TestContext testContext;
    @Value("${test.qa-user-id}")
    private String qaUserId;
    @Value("${test.qa-user-password}")
    private String qaUserPassword;
    @Getter
    private Map<String, UserDto> usersByName;

    private GameResponseDto game1;
    private GameResponseDto game2;
    private GameResponseDto createdGame;

    private static final List<ConsoleMessage> currentScenarioConsoleMessages = new ArrayList<>();
    @Value("${test.frontend-url-base}")
    private String frontendUrlBase;
    @Getter
    @Value("${test.backend-url-base}")
    private String backendUrlBase;

    private final TestProperties testProperties;

    public Page getPage() {
        return page;
    }

    @PostConstruct
    void postConstruct() {
        usersByName = Map.of(
                userName1, user1,
                userName2, user2);

        qaBackendActor = new BackendActor(testContext.getQaUserId(), userName1, testProperties.getBackendUrlBase());
        backendActor1 = new BackendActor("id-test-user-1", userName1, testProperties.getBackendUrlBase());
        backendActor2 = new BackendActor("id-test-user-2", userName2, testProperties.getBackendUrlBase());
    }

    @Before
    public void setUp() {
        currentScenarioConsoleMessages.clear();
    }

    @After
    public void tearDown() {
        deleteTestGames();
    }

    private void deleteTestGames() {
        restTemplate.delete(backendUrlBase + "/games/tests");
        testContext.deleteGame(this.game1, this);
        testContext.deleteGame(this.game2, this);
        testContext.deleteGame(this.createdGame, this);
    }

    @Given("a logged-in test user on the game-list page")
    public void logged_in_test_user() {
        if (!isOnGamesListPage(3000)) {
            log.info("redirecting user to the game-list page");
            page.navigate(frontendUrlBase + "/games", new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        }

        if (isOnAuthenticationPage(3000)) {
            page.locator("css=.firebaseui-idp-password").click();
            page.locator("css=#ui-sign-in-email-input").fill("joseph.mbimbi+test@gmail.com");
            page.locator("css=.firebaseui-id-submit").click();
            page.locator("css=#ui-sign-in-password-input").fill(qaUserPassword);
            page.locator("css=.firebaseui-id-submit").click();
        }

        PlaywrightAssertions.assertThat(page).hasURL(frontendUrlBase + "/games");
    }

    private boolean isOnGamesListPage(int timeout) {
        return waitForUrl(frontendUrlBase + "/games", timeout);
    }

    private boolean isOnAuthenticationPage(int timeout) {
        return waitForUrl(frontendUrlBase + "/authentication", timeout);
    }

    private boolean waitForUrl(String url, int timeout) {
        try {
            page.waitForURL(url, new Page.WaitForURLOptions().setTimeout(timeout));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Given("2 existing games")
    public void games() {
        String gameName1 = "test-game-1";
        game1 = createGame(gameName1, user1);
        testContext.putGame(gameName1, game1);
        String gameName2 = "test-game-2";
        game2 = createGame(gameName2, testContext.getQaUser());
        testContext.putGame(gameName2, game2);
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
                        () -> assertThat(qaFrontendActor.getDisplayedGamesForUsers(userNames, this)).isEqualTo(expected)
                );
    }

    @Then("the following games are displayed")
    public void theFollowingGamesAreDisplayed(Collection<DisplayedGame> expected) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(TestUtils.pollInterval)
                .untilAsserted(
                        () -> assertThat(qaFrontendActor.getDisplayedGamesForUsers(emptyList(), this)).isEqualTo(expected)
                );
    }


    public void userJoinsGameFromBackend(String userName, String gameName) {
        UserDto user = testContext.getUserDtoByName(userName, this);
        GameResponseDto game = testContext.getGameByName(gameName);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games/{gameId}/players/{userId}/join",
                new UserDto(user.id(), user.name()),
                GameResponseDto.class,
                game.id(),
                user.id());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @When("{string} joins {string}")
    public void userJoinsGame(String userName, String gameName) {
        if (Objects.equals(userName, testContext.getQaUser().name())) {
            qaUserClicksOnButtonForGame("join", gameName);
        } else {
            userJoinsGameFromBackend(userName, gameName);
        }
    }

    public void userStartsGameFromBackend(String userName, String gameName) {
        UserDto user = testContext.getUserDtoByName(userName, this);
        GameResponseDto game = testContext.getGameByName(gameName);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games/{gameId}/players/{userId}/start",
                new UserDto(user.id(), user.name()),
                GameResponseDto.class,
                game.id(),
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
        UserDto creator = testContext.getUserDtoByName(userName, this);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games",
                new CreateGameRequestDto(gameName, creator),
                GameResponseDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        createdGame = responseEntity.getBody();
        testContext.putGame(gameName, createdGame);
    }

    @When("{string} deletes the game named {string}")
    public void deletesTheGameNamed(String userName, String gameName) {
        UserDto creator = testContext.getUserDtoByName(userName, this);
        GameResponseDto game = testContext.getGameByName(gameName);
        restTemplate.delete(backendUrlBase + "/games/{gameId}", game.id());
        testContext.deleteGame(game, this);
    }

    private GameResponseDto getGameByName(String gameName) {
        return testContext.getGameByName(gameName);
    }

    @When("qa-user clicks on {string} button for {string}")
    public void qaUserClicksOnButtonForGame(String buttonName, String gameName) {
        Integer gameId = testContext.getGameByName(gameName).id();
        Locator button = page.getByTestId("%s-button-%d".formatted(buttonName, gameId));
        PlaywrightAssertions.assertThat(button).isVisible();
        PlaywrightAssertions.assertThat(button).isEnabled();
        button.click();
    }

    @Given("previous test data cleared")
    public void previousTestDataCleared() {
        deleteTestGames();
    }

    @And("no error is displayed in the console")
    public void noErrorIsDisplayedInTheConsole() {
        List<ConsoleMessage> errorLogs = currentScenarioConsoleMessages.stream()
                .filter(consoleMessage -> Objects.equals("error", consoleMessage.type()))
                .toList();
        String errorLogsString = errorLogs.stream().map(ConsoleMessage::text).collect(Collectors.joining("\n"));
        String failMessage = "The following error logs were present%n%s".formatted(errorLogsString);
        assertThat(errorLogs)
                .withFailMessage(() -> failMessage)
                .isEmpty();
    }

    @And("i click on game details link for {string}")
    public void noErrorIsDisplayedInTheConsole(String gameName) {
        Integer gameId = testContext.getGameByName(gameName).id();
        String link = "/games/%d/details".formatted(gameId);
        Locator locator = page.getByTestId("game-details-%d".formatted(gameId));
        PlaywrightAssertions.assertThat(locator).hasAttribute("href", link);
        locator.click();
    }

    @When("i am on the on game details page for {string}")
    public void iAmOnTheOnGameDetailsPageFor(String gameName) {
        Integer gameId = testContext.getGameByName(gameName).id();
        String url = (frontendUrlBase + "/games/%d/details").formatted(gameId);
        assertThat(waitForUrl(url, 2000)).isTrue();
    }


    @When("i directly access the game-details page for {string}")
    public void iDirectlyAccessTheGameDetailsPageFor(String gameName) {
        Integer gameId = testContext.getGameByName(gameName).id();
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
        backendActor1.join(game2);
        qaBackendActor.start(game2);
    }

    @ParameterType("(is|is not)")
    public IS_OR_NOT isOrNot(String stringValue) {
        return IS_OR_NOT.fromString(stringValue);
    }

    @When("qa-user clicks on the element with testid {string}")
    public void qaUserClicksOnTheElementWithTestid(String testId) {
        Locator locator = page.getByTestId(testId);
        PlaywrightAssertions.assertThat(locator).isVisible();
        PlaywrightAssertions.assertThat(locator).isEnabled();
        locator.click();
    }

}
