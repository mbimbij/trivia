package com.adaptionsoft.games;

import com.adaptionsoft.games.domain.TestContext;
import com.adaptionsoft.games.domain.TestProperties;
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
}
