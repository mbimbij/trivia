package com.adaptionsoft.games;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {E2eTestsSpringConfiguration.class, TestSpringEventListeners.class})
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
}
