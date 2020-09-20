package com.londonappbrewery.climapm.model;

import com.google.gson.annotations.SerializedName;

public class Sys {
    @SerializedName("sunrise")
    private Long mSunriseTime; // Unix time

    @SerializedName("sunset")
    private Long mSunsetTime; // Unix time

    public Long getSunriseTime() {
        return mSunriseTime;
    }

    public Long getSunsetTime() {
        return mSunsetTime;
    }
}
