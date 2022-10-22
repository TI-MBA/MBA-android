package com.gabia.mbaproject.model;

public class PresenceDto {

    boolean present;
    String rehearsalTitle;

    public PresenceDto(boolean present, String rehearsalTitle) {
        this.present = present;
        this.rehearsalTitle = rehearsalTitle;
    }

    public boolean wasPresent() {
        return present;
    }

    public String getRehearsalTitle() {
        return rehearsalTitle;
    }
}
