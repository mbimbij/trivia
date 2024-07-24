package com.adaptionsoft.games;

import com.adaptionsoft.games.domain.*;
import com.adaptionsoft.games.domain.pageObjects.*;
import com.adaptionsoft.games.domain.pageObjects.GameDetailsPage;
import com.adaptionsoft.games.utils.PlaywrightSingleton;
import com.microsoft.playwright.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableConfigurationProperties(TestProperties.class)
public class E2eTestsSpringConfiguration {
    @Bean
    public TestContext testContext() {
        return new TestContext();
    }

    @Bean
    public Playwright playwright() {
        return PlaywrightSingleton.getInstance();
    }

    @Bean
    public Page page(Playwright playwright) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(false)
//                .setSlowMo(1000)
                ;
        Browser browser = playwright.firefox().launch(launchOptions);
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
//                .setRecordVideoDir(Path.of("videos"))
//                .setRecordVideoSize(640, 480)
                ;
        BrowserContext newContext = browser.newContext(contextOptions);
        return newContext.newPage();
    }

    @Bean
    public Console consoleLogs(Page page) {
        return new Console(page);
    }

    @Bean
    public Janitor testRunnerActor(TestContext testContext, TestProperties testProperties) {
        return new Janitor(testProperties.getBackendUrlBase(), testContext);
    }

    @Bean
    public ActorService actorService(TestProperties testProperties) {
        return new ActorService(testProperties
        );
    }

    @Bean
    public AuthenticationPage authenticationPage(TestProperties testProperties, Page page, GamesListPage gamesListPage) {
        return new AuthenticationPage(testProperties.getFrontendUrlBase(), page, gamesListPage);
    }

    @Bean
    public GamesListPage gamesListPage(TestProperties testProperties, Page page) {
        return new GamesListPage(testProperties.getFrontendUrlBase(), page);
    }

    @Bean
    public GameRowActions gameRowActions(Page page) {
        return new GameRowActions(page);
    }

    @Bean
    public OngoingGamePage ongoingGamePage(Page page, TestProperties testProperties) {
        String urlTemplate = testProperties.getFrontendUrlBase() + "/games/%d";
        return new OngoingGamePage(page, urlTemplate);
    }

    @Bean
    public GameDetailsPage gameDetailsPage(Page page, TestProperties testProperties) {
        String urlTemplate = testProperties.getFrontendUrlBase() + "/games/%d/details";
        return new GameDetailsPage(page, urlTemplate);
    }

    @Bean
    public CreateGameUiElement createGameUiElement(Page page, TestContext testContext) {
        return new CreateGameUiElement(page, testContext);
    }

    @Bean
    public Backend backend(TestProperties testProperties) {
        return new Backend(testProperties.getBackendUrlBase());
    }
}
