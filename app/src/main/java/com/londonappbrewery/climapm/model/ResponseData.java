package com.londonappbrewery.climapm.model;

import com.google.gson.annotations.SerializedName;

public class ResponseData {

    @SerializedName("main")
    private Main mMain;

    @SerializedName("name")
    private String mCity;

    @SerializedName("weather")
    private Weather[] mWeather;

    @SerializedName("sys")
    private Sys mSys;

    @SerializedName("wind")
    private Wind mWind;


    private static String getIconName(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }

    public String getTemperature() {
        int temperature = (int) Math.round(mMain.getTemperature() - 273.15);
        return String.valueOf(temperature);
    }

    public String getCity() {
        return mCity;
    }

    public String getIconName() {
        return getIconName(mWeather[0].getConditionCode());
    }

    public Long getSunriseTime() {
        return mSys.getSunriseTime();
    }

    public Long getSunsetTime() {
        return mSys.getSunsetTime();
    }

    public Float getWindSpeed() {
        return mWind.getWindSpeed();
    }

    public int getWindDirection() {
        return mWind.getWindDirection();
    }
}
