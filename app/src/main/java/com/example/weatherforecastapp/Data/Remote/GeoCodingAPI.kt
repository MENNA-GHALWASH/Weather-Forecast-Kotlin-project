package com.example.weatherforecastapp.Data.Remote

import com.example.weatherforecastapp.Data.CityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingAPI {
    @GET("geo/1.0/direct")
    suspend fun getCities(
        @Query("q") cityName: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): List<CityResponse>
}