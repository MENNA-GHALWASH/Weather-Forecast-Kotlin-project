package com.example.weatherforecastapp.common.model

import android.util.Log
import com.example.weatherforecastapp.Data.Remote.RetrofitClient
import com.google.android.gms.common.api.internal.ApiKey

class CommonRepos private constructor() {

    private val api = RetrofitClient.reverse_geo_coding_api
    suspend fun fetchCityName(lat: Double, lon: Double,apiKey: String): String {
        return try {
            val response = api.getCityName(lat, lon, 1, apiKey)
            if (response.isNotEmpty()) {
                val city = response[0]
                "${city.name}, ${city.country}" // "Cairo, EG"
            } else {
                "Unknown Location"
            }
        } catch (e: Exception) {
            Log.e("WeatherApp", "Error fetching city: ${e.message}")
            "Unknown Location"
        }
    }

    companion object {
        @Volatile
        private var instance: CommonRepos? = null

        fun getInstance(): CommonRepos {
            return instance ?: synchronized(this) {
                instance ?: CommonRepos().also { instance = it }
            }
        }
    }
}
