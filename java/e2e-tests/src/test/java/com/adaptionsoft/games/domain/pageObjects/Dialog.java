package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;

public class Dialog extends UiElementObject {
    protected final String testId;

    public Dialog(Page page, String testId) {
        super(page);
        this.testId = testId;
    }

    public void clickOutside() {
        Locator locator = page.getByTestId(testId);
        BoundingBox boundingBox = locator.boundingBox();
        // TODO être un peu plus malin sur ce test, que ça soit robuste à la taille de l'écran et que ça ne puisse pas sortir
        double xPosition = boundingBox.x + boundingBox.width + 10;
        double yPosition = boundingBox.y;
        page.mouse().click(xPosition,yPosition);
    }

    public void verifyPresence() {
        verifyPresenceByTestId(testId);
    }

    public void verifyAbsence() {
        verifyAbsenceByTestId(testId);
    }

    public void closeByPressingEscape() {
        page.keyboard().press("Escape");
    }
}
