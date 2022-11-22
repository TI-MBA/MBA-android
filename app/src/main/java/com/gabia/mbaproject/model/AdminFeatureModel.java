package com.gabia.mbaproject.model;

import com.gabia.mbaproject.model.enums.AdminFeatureCode;

public class AdminFeatureModel {
    String featureTitle;
    AdminFeatureCode code;

    public AdminFeatureModel(String featureTitle, AdminFeatureCode code) {
        this.featureTitle = featureTitle;
        this.code = code;
    }

    public String getFeatureTitle() {
        return featureTitle;
    }

    public AdminFeatureCode getCode() {
        return code;
    }
}
