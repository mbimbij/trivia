package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.WaitForSelectorState;

public abstract class Dialog extends UiElementObject {
    protected final String testId;
    public static final String RESET = "reset";
    public static final String CANCEL = "cancel";
    public static final String VALIDATE = "validate";
    public static final String BACKEND_ERROR_MESSAGE = "backend-error-message";

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
        verifyAbsence();
    }

    public void verifyPresence() {
        verifyPresenceByTestId(testId);
    }

    public void verifyAbsence() {
        verifyAbsenceByTestId(testId);
    }

    @Override
    public void verifyAbsenceByTestId(String testId) {
        page.getByTestId(this.testId).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
    }

    public void pressEscape() {
        page.keyboard().press("Escape");
        verifyAbsence();
    }

    public void verifyBackendErrorMessagePresent() {
        verifyPresenceByTestId(BACKEND_ERROR_MESSAGE);
    }

    public void verifyValidateButtonDisabled() {
        verifyButtonDisabledByTestId(VALIDATE);
    }

    public void clickOpenDialogButton() {
        clickButtonByTestId(CreateGameDialog.OPEN_DIALOG_BUTTON);
    }

    public void clickValidate() {
        clickButtonByTestId(VALIDATE);
    }
}
