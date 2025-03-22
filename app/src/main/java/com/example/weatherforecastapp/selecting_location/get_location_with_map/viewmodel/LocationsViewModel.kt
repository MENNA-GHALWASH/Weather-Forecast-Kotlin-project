package com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.LocationsRepo
import kotlinx.coroutines.launch

class LocationsViewModel(val repo: LocationsRepo): ViewModel() {
    fun searchCities(place: String, apikey: String) {
        viewModelScope.launch {
            val cities = repo.fetchCities(place,apikey)
        }

    }
}

class LocationsViewModelFactory(val repo: LocationsRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return super.create(modelClass)
    }
}