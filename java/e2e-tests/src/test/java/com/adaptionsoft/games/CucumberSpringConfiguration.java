package com.adaptionsoft.games;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = CucumberSpringConfiguration.class)
@CucumberContextConfiguration
@ComponentScan
public class CucumberSpringConfiguration {

}
