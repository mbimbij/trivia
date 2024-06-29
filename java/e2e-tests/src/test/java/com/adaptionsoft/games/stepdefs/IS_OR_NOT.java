package com.adaptionsoft.games.stepdefs;

public enum IS_OR_NOT {
    IS("is"),
    IS_NOT("is not"),
    ;

    private final String value;

    IS_OR_NOT(String value) {
        this.value = value;
    }

    public static IS_OR_NOT fromString(String text) {
        for (IS_OR_NOT b : IS_OR_NOT.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
