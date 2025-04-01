package com.example.weatherforecastapp.Data.Remote

import com.example.weatherforecastapp.Data.CityResponse
import com.example.weatherforecastapp.Data.LocationResponse
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

interface ReverseGeocodingApi {
    @GET("geo/1.0/reverse")
    suspend fun getCityName(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): List<LocationResponse> // Returns a list (because API returns an array)
}
