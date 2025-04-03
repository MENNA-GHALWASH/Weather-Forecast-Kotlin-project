package com.example.weatherforecastapp.home.daily_forecast.model

import android.content.Context
import android.util.Log
import com.example.weatherforecastapp.Data.DailyForecast
import com.example.weatherforecastapp.Data.HourlyForecast
import com.example.weatherforecastapp.Data.Remote.RetrofitClient
import com.example.weatherforecastapp.Data.WeatherResponse
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.UNITS
import com.example.weatherforecastapp.settings.model.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WeatherRepo(private val weatherDao: WeatherDao,private val context: Context) {


    suspend fun getCurrentWeather(lat: Double, lon: Double, apikey: String): WeatherResponse {
        return try {
            val weatherAPI = RetrofitClient.one_call_api

            val units = SettingsManager.getTemperatureUnit(context)
            val language = SettingsManager.getLanguage(context).toString().lowercase()  // Assuming you need the language in lowercase (e.g., "en" for English)

            val response = weatherAPI.getCurrentWeather(lat, lon, "current", apikey, units.name, language)
            weatherDao.insertWeatherResponse(response)
            response
        } catch (e: Exception) {
            weatherDao.getWeatherResponse(lat, lon)
        }
    }

}
