package com.example.weatherforecastapp.home.daily_forecast.model

import android.util.Log
import com.example.weatherforecastapp.Data.DailyForecast
import com.example.weatherforecastapp.Data.HourlyForecast
import com.example.weatherforecastapp.Data.Remote.RetrofitClient
import com.example.weatherforecastapp.Data.WeatherResponse
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.UNITS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WeatherRepo(private val weatherDao: WeatherDao) {

    var units = UNITS.METRIC

    suspend fun getCurrentWeather(lat: Double, lon: Double, apikey: String): WeatherResponse? {
        return try {
            val weatherAPI = RetrofitClient.one_call_api
            val response = weatherAPI.getCurrentWeather(lat, lon, "current", apikey)
            weatherDao.insertWeatherResponse(response)
            response
        } catch (e: Exception) {
            weatherDao.getWeatherResponse(lat, lon)
        }
    }

    //get current weather -> ...,...,..,.units
    //units will change from settings
    //i need to get units and their corresponding letters to display onscreen


//    //move to weather repo
//    suspend fun getWeatherbyHour(lat:Double, lon:Double, apikey:String): HourlyForecast? { //should return something
//        try {
//            val weatherAPI = RetrofitClient.one_call_api
//
//            if (units== UNITS.STANDARD){ return  weatherAPI.getHourlyWeather(lat,lon,"hourly",apikey,"standard") }
//            else if (units== UNITS.IMPERIAL){ return  weatherAPI.getHourlyWeather(lat,lon,"hourly",apikey,"imperial") }
//            else { return  weatherAPI.getHourlyWeather(lat,lon,"hourly",apikey) }
//
//        } catch (e:Exception){
//            Log.e("Weather", "getWeatherbyHour: ${e.message}", )
//            return null
//        }
//    }
//
//    suspend fun getWeatherOverDays(lat:Double, lon:Double, apikey:String): DailyForecast? {
//        try {
//            val weatherAPI = RetrofitClient.one_call_api
//            return weatherAPI.getDailyWeather(lat,lon,"daily",apikey)
//
//        } catch (e:Exception){
//            Log.e("Weather", "getWeatherOverDays: ${e.message}", )
//            return null
//        }
//
//    }

    fun getUnit():String{
        when(units){
            UNITS.STANDARD ->  return "K"
            UNITS.METRIC -> return "C"
            UNITS.IMPERIAL -> return "F"
        }
    }
}
