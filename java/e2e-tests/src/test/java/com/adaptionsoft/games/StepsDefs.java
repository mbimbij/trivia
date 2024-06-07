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
import java.util.stream.Collectors;

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
    private  UserDto qaUser;
    private Map<String, UserDto> usersByName;

    private GameResponseDto game1;
    private GameResponseDto game2;
    private GameResponseDto createdGame;

    private final Map<String, GameResponseDto> gamesByName = new HashMap<>();
    private static final List<ConsoleMessage> currentScenarioConsoleMessages = new ArrayList<>();

    @PostConstruct
    void postConstruct(){
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
        restTemplate.delete("http://localhost:8080/games/tests");
        deleteGame(this.game1);
        deleteGame(this.game2);
        deleteGame(this.createdGame);
    }

    private void deleteGame(GameResponseDto game) {
        if (game != null) {
            restTemplate.delete("http://localhost:8080/games/{gameId}", game.id());
            gamesByName.remove(game.name());
        }
    }

    @Given("a logged-in test user on the game-list page")
    public void logged_in_test_user() {
        if (!isOnGamesListPage(1000)) {
            log.info("redirecting user to the game-list page");
            page.navigate("http://localhost:4200/games", new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        }

        if (isOnAuthenticationPage(1000)) {
            page.locator("css=.firebaseui-idp-password").click();
            page.locator("css=#ui-sign-in-email-input").fill("joseph.mbimbi+test@gmail.com");
            page.locator("css=.firebaseui-id-submit").click();
            page.locator("css=#ui-sign-in-password-input").fill(qaUserPassword);
            page.locator("css=.firebaseui-id-submit").click();
        }

        PlaywrightAssertions.assertThat(page).hasURL("http://localhost:4200/games");
    }

    private boolean isOnGamesListPage(int timeout) {
        return waitForUrl("http://localhost:4200/games", timeout);
    }

    private boolean isOnAuthenticationPage(int timeout) {
        return waitForUrl("http://localhost:4200/authentication", timeout);
    }

    private boolean isOnUrl(String url) {
        try {
            assertThat(page.evaluate("window.location.href")).isEqualTo(url);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    private boolean waitForUrl(String url, int timeout) {
//        try {
//            await().atMost(Duration.ofMillis(timeout))
//                    .pollInterval(Duration.ofMillis(500))
//                    .untilAsserted(
//                            () ->
//                                    assertThat(page.evaluate("window.location.href")).isEqualTo(url));
//            return true;
//        } catch (Throwable e) {
//            return false;
//        }
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
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity("http://localhost:8080/games",
                requestDto,
                GameResponseDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return responseEntity.getBody();
    }

    @Then("the following games are displayed for users \"{strings}\"")
    public void theFollowingGamesAreDisplayed(Collection<String> userNames, Collection<DisplayedGame> expectedDisplayedGames) {
        await().atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> assertThat(page.querySelectorAll(".game-row").stream()
                        .filter(h -> userNames.contains(h.querySelector(".creator-name").textContent().trim()))
                        .map(this::convertToObject)
                        .toList()).isEqualTo(expectedDisplayedGames));

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

    @When("{string} joins {string}")
    public void userJoinsGame(String userName, String gameName) {
        UserDto user = getUserByName(userName);
        GameResponseDto game = getGameByName(gameName);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity("http://localhost:8080/games/{gameId}/players/{userId}/join",
                new UserDto(user.id(), user.name()),
                GameResponseDto.class,
                game.id(),
                user.id());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @When("test-user-1 starts test-game-1")
    public void testUserStartsTestGame() {
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity("http://localhost:8080/games/{gameId}/players/{userId}/start",
                new UserDto(user1.id(), user1.name()),
                GameResponseDto.class,
                game1.id(),
                user1.id());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @When("{string} creates a game named {string}")
    public void testUserCreatesAGameNamed(String userName, String gameName) {
        UserDto creator = getUserByName(userName);
        ResponseEntity<GameResponseDto> responseEntity = restTemplate.postForEntity("http://localhost:8080/games",
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
        restTemplate.delete("http://localhost:8080/games/{gameId}",game.id());
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
    public void qaUserClicksOnStartButtonFor(String buttonName, String gameName) {
        Integer gameId = getGameByName(gameName).id();
        Locator startButton = page.getByTestId("%s-button-%d".formatted(buttonName, gameId));
        PlaywrightAssertions.assertThat(startButton).isVisible();
        startButton.click();
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
}
