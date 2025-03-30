package com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weatherforecastapp.Data.CityResponse
import com.example.weatherforecastapp.Data.DailyForecast
import com.example.weatherforecastapp.Data.HourlyForecast
import com.example.weatherforecastapp.Data.WeatherResponse
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.LocationsRepo
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.UNITS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationsViewModel(private val repo: LocationsRepo) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> get() = _searchResults

    //for city response
    private val _city_resp = MutableStateFlow<List<CityResponse>>(emptyList())//not sure if this is right
    val city_resp:StateFlow<List<CityResponse>> get() = _city_resp

////
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _current_weather = MutableStateFlow<WeatherResponse?>(null)//not sure if this is right
    val current_weather:StateFlow<WeatherResponse?> get() = _current_weather

    private val _daily_weather = MutableStateFlow<DailyForecast?>(null)//not sure if this is right
    val daily_weather:StateFlow<DailyForecast?> get() = _daily_weather

    private val _hourly_weather = MutableStateFlow<HourlyForecast?>(null)//not sure if this is right
    val hourly_weather:StateFlow<HourlyForecast?> get() = _hourly_weather
////


    fun searchCities(place: String, apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cities = repo.fetchCitiesStrings(place, apiKey)
                _searchResults.value = cities

                Log.i("Cities", "searchCities: $cities")
            } catch (e: Exception) {
                _searchResults.value = listOf("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCities(place: String, apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cities = repo.fetchCities(place, apiKey)
                _city_resp.value = cities

                Log.i("Cities", "searchCities: $cities")
            } catch (e: Exception) {
                _searchResults.value = listOf("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }



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

    fun getHourlyWeather(lat:Double,lon:Double,apiKey: String){
        viewModelScope.launch {
            try {
                val weatherResp = repo.getWeatherbyHour(lat,lon,apiKey)
                _hourly_weather.value = weatherResp

                Log.i("Weather", "getHourlyWeather: $weatherResp")
            } catch (e: Exception){
                _hourly_weather.value = null
            }
            //you may add finally
        }
    }

    fun getDailyWeather(lat:Double,lon:Double,apiKey: String){
        viewModelScope.launch {
            try {
                val weatherResp = repo.getWeatherOverDays(lat,lon,apiKey)
                _daily_weather.value = weatherResp

                Log.i("Weather", "getDailyWeather: $weatherResp")
            } catch (e: Exception){
                _daily_weather.value = null
            }
            //you may add finally
        }
    }

    fun setUnit(unit:UNITS){
        repo.units == unit
    }

    fun getUnit():String{
        return repo.getUnit()
    }
}


//class LocationsViewModelFactory(val repo: LocationsRepo): ViewModelProvider.Factory{
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return super.create(modelClass)
//    }
//}
class LocationsViewModelFactory(
    private val repo: LocationsRepo
) : ViewModelProvider.Factory {

   // @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (modelClass.isAssignableFrom(LocationsViewModel::class.java)) {
            return LocationsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}