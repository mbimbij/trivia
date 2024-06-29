package com.adaptionsoft.games.utils;

import com.adaptionsoft.games.stepdefs.OngoingGameStepsDefs;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TestOfTests {

    @Test
    void test_logs_verification() {
        List<String> actualLogs = List.of("Game Id(value=1) started",
                "Chet is the current player",
                "They have rolled a 5",
                "Chet's new location is 5",
                "The category is Pop",
                "question Pop 0"
        );
        List<String> expectedLogs = List.of("They have rolled a \\d",
                ".*'s new location is \\d+",
                "The category is .*",
                "question .*"
        );
        OngoingGameStepsDefs.verifyGameLogsMatch(actualLogs, expectedLogs);
    }

    @Test
    void regexTests() {
        String log = "Game Id(value=3) started";
        String regex = "Game Id\\(value=[0-9]*\\) started";
        assertThat(log).matches(regex);
    }
}