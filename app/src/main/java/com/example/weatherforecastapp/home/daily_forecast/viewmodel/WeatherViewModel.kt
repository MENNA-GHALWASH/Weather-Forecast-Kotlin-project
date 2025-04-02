package com.example.weatherforecastapp.home.daily_forecast.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.Data.DailyForecast
import com.example.weatherforecastapp.Data.HourlyForecast
import com.example.weatherforecastapp.Data.WeatherResponse
import com.example.weatherforecastapp.home.daily_forecast.model.WeatherRepo
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.UNITS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(val repo: WeatherRepo):ViewModel() {

    private val _current_weather = MutableStateFlow<WeatherResponse?>(null)//not sure if this is right
    val current_weather: StateFlow<WeatherResponse?> get() = _current_weather

//    private val _daily_weather = MutableStateFlow<DailyForecast?>(null)//not sure if this is right
//    val daily_weather: StateFlow<DailyForecast?> get() = _daily_weather
//
//    private val _hourly_weather = MutableStateFlow<HourlyForecast?>(null)//not sure if this is right
//    val hourly_weather: StateFlow<HourlyForecast?> get() = _hourly_weather


    fun getCurrentWeather(lat:Double,lon:Double,apiKey: String){
        viewModelScope.launch {
            try {
                val weatherResp = repo.getCurrentWeather(lat,lon,apiKey)
                _current_weather.value = weatherResp

                Log.i("Weather", "getCurrentWeather: $weatherResp")
            } catch (e: Exception){
                _current_weather.value = null
            }
            //you may add finally
        }
    }

    fun setUnit(unit: UNITS){
        repo.units = unit
    }

    fun getUnit():String{
        return repo.getUnit()
    }
}
class WeatherViewModelFactory(private val repo: WeatherRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}