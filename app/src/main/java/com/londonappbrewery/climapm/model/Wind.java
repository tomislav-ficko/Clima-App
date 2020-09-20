package com.londonappbrewery.climapm.model;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    private Float mWindSpeed;

    @SerializedName("deg")
    private int mWindDirection;

    public Float getWindSpeed() {
        return mWindSpeed;
    }

    public int getWindDirection() {
        return mWindDirection;
    }
}
