package com.adaptionsoft.games;

import com.adaptionsoft.games.domain.FrontendActor;
import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.TestProperties;
import com.adaptionsoft.games.domain.TestRunnerActor;
import com.microsoft.playwright.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableConfigurationProperties(TestProperties.class)
public class E2eTestsSpringConfiguration {
    @Bean
    public TestContext testContext(TestProperties testProperties) {
        return new TestContext(testProperties.getFrontendUrlBase(), testProperties.getBackendUrlBase());
    }

    @Bean
    public Playwright playwright() {
        return Playwright.create();
    }

    @Bean
    public Page qaFrontendActorPage(Playwright playwright) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(false)
//                .setSlowMo(1000)
                ;
        Browser browser = playwright.firefox().launch(launchOptions);
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        BrowserContext newContext = browser.newContext(contextOptions);
        return newContext.newPage();
    }

    @Bean
    public FrontendActor qaFrontendActor(TestContext testContext, Page page){
        FrontendActor frontendActor = new FrontendActor(testContext.getQaUserId(), testContext.getQaUserName(), page, testContext);
        frontendActor.registerBrowserLogs();
        return frontendActor;
    }

    @Bean
    public TestRunnerActor testRunnerActor(TestContext testContext){
        return new TestRunnerActor(testContext.getBackendUrlBase());
    }
}
