package com.adaptionsoft.games.domain.pageObjects;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RenameUser extends UiElementObject {
    public static final String INPUT = "userName";

    public RenameUser(Page page) {
        super(page);
    }

    public void renameUser(String newName) {
        page.getByTestId(INPUT).fill(newName);
        page.keyboard().press("Enter");
    }
}
