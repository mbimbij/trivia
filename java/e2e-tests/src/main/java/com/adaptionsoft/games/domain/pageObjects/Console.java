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
                .filter(m -> Objects.equals("error", m.type()))
                .map(m -> m.location() + " - " + m.text())
                .toList();
    }

    public void clearLogs() {
        getCurrentScenarioLogs().clear();
    }
}
