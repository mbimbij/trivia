package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.ConsoleMessage;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Console {
    private final List<ConsoleMessage> currentScenarioLogs = new ArrayList<>();

    public Console(Page page) {
        page.onConsoleMessage(e -> {
            getCurrentScenarioLogs().add(e);
        });
    }

    public synchronized List<ConsoleMessage> getCurrentScenarioLogs() {
        return currentScenarioLogs;
    }

    public List<String> getErrorLogs() {
        return getCurrentScenarioLogs().stream()
                .filter(consoleMessage -> Objects.equals("error", consoleMessage.type()))
                .map(consoleMessage -> consoleMessage.text()+consoleMessage.location())
                .toList();
    }

    public void clearLogs() {
        getCurrentScenarioLogs().clear();
    }
}
