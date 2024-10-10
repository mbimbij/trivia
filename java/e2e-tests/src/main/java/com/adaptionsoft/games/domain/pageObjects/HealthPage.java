package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthPage extends PageWithStaticUrl {
    public static final String HEALTH_STATUS_TESTID = "health";
    public static final String HEALTH_STATUS_TEXT = "frontend is up";

    private boolean frontendIsUpVerified = false;

    public HealthPage(String basePath, Page page) {
        super(basePath + "/health", page);
    }

    public void verifyFrontendIsUp() {
        if (!frontendIsUpVerified) {
            try {
                navigateTo();
                String textContent = getTextContentByTestid(HEALTH_STATUS_TESTID);
                assertThat(textContent).isEqualTo(HEALTH_STATUS_TEXT);
            } catch (Exception e) {
                System.err.println("************************************");
                System.err.println("Frontend is not up. Ending tests");
                System.err.println("************************************");
                System.exit(-1);
            }
            frontendIsUpVerified = true;
        }
    }
}
