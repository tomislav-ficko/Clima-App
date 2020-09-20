package com.londonappbrewery.climapm.model;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("id")
    private int mConditionCode;

    public int getConditionCode() {
        return mConditionCode;
    }
}
