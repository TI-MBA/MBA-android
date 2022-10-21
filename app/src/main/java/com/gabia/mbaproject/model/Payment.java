package com.gabia.mbaproject.model;

import java.util.Date;

public class Payment {
    private Float value;
    private String observation;
    private Date relativeDate;

    public Payment(Float value, String observation, Date relativeDate) {
        this.value = value;
        this.observation = observation;
        this.relativeDate = relativeDate;
    }

    public Float getValue() {
        return value;
    }

    public String getObservation() {
        return observation;
    }

    public Date getRelativeDate() {
        return relativeDate;
    }
}
