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
import com.example.weatherforecastapp.common.model.CommonRepos
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading


    val _city_name =  MutableStateFlow<String>("")
    val city_name: StateFlow<String> get() = _city_name

    private val commonRepos = CommonRepos.getInstance()


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
        //works properly
    }

    fun getCityName(lat: Double, lon: Double, apiKey: String){
          viewModelScope.launch {
              val rep =  commonRepos.fetchCityName(lat, lon, apiKey)
              _city_name.value = rep
          }
        }
    }


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