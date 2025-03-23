package com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.LocationsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationsViewModel(private val repo: LocationsRepo) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> get() = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun searchCities(place: String, apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cities = repo.fetchCities(place, apiKey)
                _searchResults.value = cities
                Log.i("Cities", "searchCities: $cities")
            } catch (e: Exception) {
                _searchResults.value = listOf("Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
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

    @Suppress("UNCHECKED_CAST")
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