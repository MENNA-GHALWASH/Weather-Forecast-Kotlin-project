package com.example.weatherforecastapp.selecting_location.get_location_with_map.model

import android.util.Log
import com.example.weatherforecastapp.Data.DailyForecast
import com.example.weatherforecastapp.Data.HourlyForecast
import com.example.weatherforecastapp.Data.Remote.RetrofitClient
import com.example.weatherforecastapp.Data.WeatherResponse

class LocationsRepo {

    suspend fun fetchCities(place: String, apikey:String): List<String> {
        try {
            val geoCodingAPI = RetrofitClient.geo_coding_api
            val response = geoCodingAPI.getCities(/*String.contains place*/place, 5, apikey)
            return response.map { "${it.name}, ${it.country} , ${it.lat} , ${it.lon}" }

        } catch (e: Exception){
            return listOf("Error fetching data")
        }
    }

    suspend fun getCurrentWeather(lat:Double, lon:Double, apikey:String): WeatherResponse? { //should return something
        return try {
            val weatherAPI = RetrofitClient.one_call_api
             weatherAPI.getCurrentWeather(lat,lon,"current",apikey)

        } catch (e:Exception){
            val empty: WeatherResponse? = null
            return empty
        }
    }



    suspend fun getWeatherbyHour(lat:Double, lon:Double, apikey:String): HourlyForecast? { //should return something
        try {
            val weatherAPI = RetrofitClient.one_call_api
            return  weatherAPI.getHourlyWeather(lat,lon,"hourly",apikey)

        } catch (e:Exception){
            Log.e("Weather", "getWeatherbyHour: ${e.message}", )
            return null
        }
    }

    suspend fun getWeatherOverDays(lat:Double, lon:Double, apikey:String): DailyForecast? {
        try {
            val weatherAPI = RetrofitClient.one_call_api
            return weatherAPI.getDailyWeather(lat,lon,"daily",apikey)

        } catch (e:Exception){
            Log.e("Weather", "getWeatherOverDays: ${e.message}", )
            return null
        }
    }
}