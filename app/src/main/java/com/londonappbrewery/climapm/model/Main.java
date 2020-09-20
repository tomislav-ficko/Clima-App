package com.londonappbrewery.climapm.model;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    private Float mTemperature;

    public Float getTemperature() {
        return mTemperature;
    }
}
