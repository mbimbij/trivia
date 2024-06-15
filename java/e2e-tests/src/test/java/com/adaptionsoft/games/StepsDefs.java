package com.adaptionsoft.games;

import com.adaptionsoft.games.trivia.web.CreateGameRequestDto;
import com.adaptionsoft.games.trivia.web.GameResponseDto;
import com.adaptionsoft.games.trivia.web.UserDto;
import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.WaitUntilState;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class StepsDefs {

    private static final Logger log = LoggerFactory.getLogger(StepsDefs.class);
    private static Playwright playwright;
    private static Page page;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String userName1 = "test-user-1";
    private final String userName2 = "test-user-2";
    private final String qaUserName = "qa-user";
    private final UserDto user1 = new UserDto("id-test-user-1", userName1);
    private final UserDto user2 = new UserDto("id-test-user-2", userName2);
    @Value("${application.qa-user.id}")
    private String qaUserId;
    @Value("${application.qa-user.password}")
    private String qaUserPassword;
    private UserDto qaUser;
    private Map<String, UserDto> usersByName;

    private GameResponseDto game1;
    private GameResponseDto game2;
    private GameResponseDto createdGame;

    private final Map<String, GameResponseDto> gamesByName = new HashMap<>();
    private static final List<ConsoleMessage> currentScenarioConsoleMessages = new ArrayList<>();
    @Value("${application.frontend-url-base}")
    private String frontendUrlBase;
    @Value("${application.backend-url-base}")
    private String backendUrlBase;

    @PostConstruct
    void postConstruct() {
        qaUser = new UserDto(qaUserId, qaUserName);
        usersByName = Map.of(
                userName1, user1,
                userName2, user2,
                qaUserName, qaUser);
    }

    @BeforeAll
    public static void beforeAll() throws Exception {
        playwright = Playwright.create();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setFirefoxUserPrefs(
                        Map.of(
                                "network.websocket.allowInsecureFromHTTPS", true
                        )
                )
//                .setSlowMo(1000)
                ;
        Browser browser = playwright.firefox().launch(launchOptions);
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        BrowserContext newContext = browser.newContext(contextOptions);
        page = newContext.newPage();
        page.onConsoleMessage(currentScenarioConsoleMessages::add);
    }

    @Before
    public void setUp() {
        currentScenarioConsoleMessages.clear();
    }

    @AfterAll
    public static void afterAll() {
        playwright.close();
    }

    @After
    public void tearDown() {
        deleteTestGames();
    }

    private void deleteTestGames() {
        restTemplate.delete(backendUrlBase + "/games/tests");
        deleteGame(this.game1);
        deleteGame(this.game2);
        deleteGame(this.createdGame);
    }

    private void deleteGame(GameResponseDto game) {
        if (game != null) {
            restTemplate.delete(backendUrlBase + "/games/{gameId}", game.id());
            gamesByName.remove(game.name());
        }
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

    @Given("a test user")
    public void anTestUser() {
    }

    @Given("2 existing games")
    public void games() {
        String gameName1 = "test-game-1";
        game1 = createGame(gameName1, user1);
        gamesByName.put(gameName1, game1);
        String gameName2 = "test-game-2";
        game2 = createGame(gameName2, qaUser);
        gamesByName.put(gameName2, game2);
    }

    private GameResponseDto createGame(String gameName, UserDto user) {
        CreateGameRequestDto requestDto = new CreateGameRequestDto(gameName, user);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games",
                requestDto,
                GameResponseDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return responseEntity.getBody();
    }

    @Then("the following games are displayed for users \"{strings}\"")
    public void theFollowingGamesAreDisplayedForUsers(Collection<String> userNames, Collection<DisplayedGame> expected) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(
                        () -> assertThat(getDisplayedGamesForUsers(userNames)).isEqualTo(expected)
                );
    }

    @Then("the following games are displayed")
    public void theFollowingGamesAreDisplayed(Collection<DisplayedGame> expected) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(
                        () -> assertThat(getDisplayedGamesForUsers(emptyList())).isEqualTo(expected)
                );
    }

    /**
     * @param userNames Pass null or an empty collection to get games for all users
     * @return
     */
    private List<DisplayedGame>
    getDisplayedGamesForUsers(Collection<String> userNames) {
        Predicate<ElementHandle> predicate = (userNames == null || userNames.isEmpty())
                ? h -> true
                : h -> userNames.contains(h.querySelector(".creator-name").textContent().trim());
        return page.querySelectorAll(".game-row").stream()
                .filter(predicate)
                .map(this::convertToObject)
                .toList();
    }

    public DisplayedGame convertToObject(ElementHandle elementHandle) {
        return new DisplayedGame(
                elementHandle.querySelector(".name").textContent().trim(),
                elementHandle.querySelector(".creator-name").textContent().trim(),
                elementHandle.querySelector(".players-names").textContent().trim(),
                elementHandle.querySelector(".state").textContent().trim(),
                getButtonState(elementHandle, "start"),
                getButtonState(elementHandle, "join"),
                elementHandle.querySelector(".join").textContent().trim(),
                getButtonState(elementHandle, "goto"),
                getButtonState(elementHandle, "delete")
        );
    }

    private Boolean getButtonState(ElementHandle elementHandle, String buttonName) {
        return Optional.ofNullable(elementHandle.querySelector("." + buttonName + " button")).map(ElementHandle::isEnabled).orElse(null);
    }

    @DataTableType
    public Collection<DisplayedGame> displayedGames(DataTable dataTable) {
        return TestUtils.convertDatatableList(dataTable, DisplayedGame.class);
    }

    @ParameterType(
            value = ".+",
            name = "strings")
    public Collection<String> strings(String string) {
        return Arrays.stream(string.trim().split("\\s*,\\s*")).collect(Collectors.toCollection(ArrayList::new));
    }

    public void userJoinsGameFromBackend(String userName, String gameName) {
        UserDto user = getUserByName(userName);
        GameResponseDto game = getGameByName(gameName);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games/{gameId}/players/{userId}/join",
                new UserDto(user.id(), user.name()),
                GameResponseDto.class,
                game.id(),
                user.id());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @When("{string} joins {string}")
    public void userJoinsGame(String userName, String gameName) {
        if (Objects.equals(userName, qaUser.name())) {
            qaUserClicksOnButtonForGame("join", gameName);
        } else {
            userJoinsGameFromBackend(userName, gameName);
        }
    }

    public void userStartsGameFromBackend(String userName, String gameName) {
        UserDto user = getUserByName(userName);
        GameResponseDto game = getGameByName(gameName);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games/{gameId}/players/{userId}/start",
                new UserDto(user.id(), user.name()),
                GameResponseDto.class,
                game.id(),
                user.id());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @When("{string} starts {string}")
    public void testUserStartsTestGame(String userName, String gameName) {
        if (Objects.equals(qaUser.name(), userName)) {
            qaUserClicksOnButtonForGame("start", gameName);
        } else {
            userStartsGameFromBackend(userName, gameName);
        }
    }

    @When("{string} creates a game named {string}")
    public void testUserCreatesAGameNamed(String userName, String gameName) {
        UserDto creator = getUserByName(userName);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity(backendUrlBase + "/games",
                new CreateGameRequestDto(gameName, creator),
                GameResponseDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        createdGame = responseEntity.getBody();
        gamesByName.put(gameName, createdGame);
    }

    @When("{string} deletes the game named {string}")
    public void deletesTheGameNamed(String userName, String gameName) {
        UserDto creator = getUserByName(userName);
        GameResponseDto game = getGameByName(gameName);
        restTemplate.delete(backendUrlBase + "/games/{gameId}", game.id());
        deleteGame(game);
    }

    private UserDto getUserByName(String userName) {
        assertThat(usersByName).containsKey(userName);
        return usersByName.get(userName);
    }

    private GameResponseDto getGameByName(String gameName) {
        assertThat(gamesByName).containsKey(gameName);
        return gamesByName.get(gameName);
    }

    @When("qa-user clicks on {string} button for {string}")
    public void qaUserClicksOnButtonForGame(String buttonName, String gameName) {
        Integer gameId = getGameByName(gameName).id();
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
        Integer gameId = getGameByName(gameName).id();
        String link = "/games/%d/details".formatted(gameId);
        Locator locator = page.getByTestId("game-details-%d".formatted(gameId));
        PlaywrightAssertions.assertThat(locator).hasAttribute("href", link);
        locator.click();
    }

    @When("i am on the on game details page for {string}")
    public void iAmOnTheOnGameDetailsPageFor(String gameName) {
        Integer gameId = getGameByName(gameName).id();
        String url = (frontendUrlBase + "/games/%d/details").formatted(gameId);
        assertThat(waitForUrl(url, 2000)).isTrue();
    }

    @When("i am on the on game page for {string}")
    public void iAmOnTheOnGamePageFor(String gameName) {
        Integer gameId = getGameByName(gameName).id();
        String url = (frontendUrlBase + "/games/%d").formatted(gameId);
        assertThat(waitForUrl(url, 2000)).isTrue();
    }

    @When("i directly access the game-details page for {string}")
    public void iDirectlyAccessTheGameDetailsPageFor(String gameName) {
        Integer gameId = getGameByName(gameName).id();
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
        userJoinsGameFromBackend(user1.name(), game2.name());
        userStartsGameFromBackend(qaUser.name(), game2.name());
    }

    @And("the element with testid {string} is visible")
    public void theFollowingElementsAreVisible(String testId) {
        PlaywrightAssertions.assertThat(page.getByTestId(testId)).isVisible();
    }
}
