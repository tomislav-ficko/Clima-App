package com.londonappbrewery.climapm.network;

import com.londonappbrewery.climapm.model.ResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("data/2.5/weather")
    Call<ResponseData> getWeatherForLocation(
            @Query("lat") String latitude,
            @Query("lon") String longitude,
            @Query("appid") String appid);

    @GET("data/2.5/weather")
    Call<ResponseData> getWeatherForCity(
            @Query("q") String cityName,
            @Query("appid") String appid);
}
