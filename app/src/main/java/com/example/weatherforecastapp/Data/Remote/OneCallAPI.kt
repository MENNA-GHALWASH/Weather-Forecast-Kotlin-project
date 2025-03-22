package com.example.weatherforecastapp.Data.Remote

import com.example.weatherforecastapp.Data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OneCallAPI {
    @GET("data/3.0/onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}