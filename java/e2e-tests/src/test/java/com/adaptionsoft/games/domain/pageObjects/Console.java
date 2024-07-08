package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.ConsoleMessage;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Console {
    private final List<ConsoleMessage> currentScenarioLogs = new ArrayList<>();

    public Console(Page page) {
        page.onConsoleMessage(currentScenarioLogs::add);
    }

    public List<String> getErrorLogs() {
        return currentScenarioLogs.stream()
                .filter(consoleMessage -> Objects.equals("error", consoleMessage.type()))
                .map(ConsoleMessage::text)
                .toList();
    }

    public void clearLogs() {
        currentScenarioLogs.clear();
    }

    public List<String> findLogsMatching(String regex){
        Pattern pattern = Pattern.compile(regex);
        return currentScenarioLogs.stream()
                .map(ConsoleMessage::text)
                .filter(s -> pattern.matcher(s).find())
                .toList();
    }
}
