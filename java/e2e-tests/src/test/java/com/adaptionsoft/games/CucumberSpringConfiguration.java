package com.adaptionsoft.games;

import com.adaptionsoft.games.trivia.web.WebConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
        E2eTestsSpringConfiguration.class
})
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
}
