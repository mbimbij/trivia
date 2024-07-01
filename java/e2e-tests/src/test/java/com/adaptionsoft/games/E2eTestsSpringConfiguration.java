package com.adaptionsoft.games;

import com.adaptionsoft.games.domain.*;
import com.adaptionsoft.games.utils.PlaywrightSingleton;
import com.microsoft.playwright.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.nio.file.Path;

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
    public Page qaFrontendActorPage(Playwright playwright) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(false)
//                .setSlowMo(1000)
                ;
        Browser browser = playwright.firefox().launch(launchOptions);
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setRecordVideoDir(Path.of("videos"))
                .setRecordVideoSize(640, 480);
        BrowserContext newContext = browser.newContext(contextOptions);
        return newContext.newPage();
    }

    @Bean
    public FrontendActor qaFrontendActor(Page page, TestProperties testProperties) {
        FrontendActor frontendActor = new FrontendActor(testProperties.getQaUserId(),
                TestContext.QA_FRONTEND_USER_NAME,
                page,
                testProperties.getFrontendUrlBase(),
                testProperties.getQaUserEmail(),
                testProperties.getQaUserPassword()
        );
        frontendActor.registerBrowserLogs();
        return frontendActor;
    }

    @Bean
    public Janitor testRunnerActor(TestContext testContext, TestProperties testProperties) {
        return new Janitor(testProperties.getBackendUrlBase(), testContext);
    }

    @Bean
    public BackendActor qaBackendActor(TestProperties testProperties) {
        return new BackendActor(testProperties.getQaUserId(),
                TestContext.QA_FRONTEND_USER_NAME,
                testProperties.getBackendUrlBase()
        );
    }

    @Bean
    public BackendActor backendActor1(TestProperties testProperties) {
        return new BackendActor(TestContext.ID_TEST_USER_1,
                TestContext.TEST_USER_NAME_1,
                testProperties.getBackendUrlBase()
        );
    }

    @Bean
    public BackendActor backendActor2(TestProperties testProperties) {
        return new BackendActor(TestContext.ID_TEST_USER_2,
                TestContext.TEST_USER_NAME_2,
                testProperties.getBackendUrlBase()
        );
    }

    @Bean
    public ActorService actorService(FrontendActor qaFrontendActor,
                                     BackendActor qaBackendActor,
                                     BackendActor backendActor1,
                                     BackendActor backendActor2) {
        return new ActorService(qaFrontendActor,
                qaBackendActor,
                backendActor1,
                backendActor2);
    }
}
