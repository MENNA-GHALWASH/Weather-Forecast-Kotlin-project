package com.example.weatherforecastapp.favourites.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weatherforecastapp.Data.CityResponse
import com.example.weatherforecastapp.favourites.model.FavClass
import com.example.weatherforecastapp.favourites.model.FavouritesRepo
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavouritesViewModel(var repo: FavouritesRepo):ViewModel() {

    val allFavourites: StateFlow<List<FavClass>> = repo.loadFavourites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun addToFavourites(city: FavClass) {
        viewModelScope.launch {
            repo.addFavourite(city)
            Log.i("Favourites", "Added: ${city.city}")
        }
    }

    fun deleteFavourite(favCountry: FavClass){
        viewModelScope.launch {
            repo.deleteFavourites(favCountry)
        }
    }
    //add coroutines
}

class FavouritesViewModelFactory(val repo:FavouritesRepo):ViewModelProvider.Factory{

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)) {
            return FavouritesViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.weatherforecastapp.common.model.Response
//import com.example.weatherforecastapp.favourites.model.FavClass
//import com.example.weatherforecastapp.favourites.model.FavouritesRepo
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//
//
//class FavouritesViewModel(var repo: FavouritesRepo): ViewModel() {
//
//    private val _allFavourites = MutableStateFlow<Response<List<FavClass>>>(Response.Loading)
//    val allFavourites: StateFlow<Response<List<FavClass>>> = _allFavourites
//
//    init {
//        loadFavourites()
//    }
//
//    private fun loadFavourites() {
//
//        viewModelScope.launch {
//            _allFavourites.value = Response.Loading
//            try {
//                val favourites = repo.loadFavourites().first()
//                _allFavourites.value = Response.Success(favourites)
//            } catch (ex: Exception) {
//                _allFavourites.value = Response.Failure(ex)
//            }
//        }
//    }
//
//    fun addToFavourites(city: FavClass) {
//        viewModelScope.launch {
//            try {
//                repo.addFavourite(city)
//                Log.i("Favourites", "Added: ${city.city}")
//                loadFavourites()
//            } catch (ex: Exception) {
//                _allFavourites.value = Response.Failure(ex)
//            }
//        }
//    }
//
//    fun deleteFavourite(favCountry: FavClass) {
//        viewModelScope.launch {
//            try {
//                repo.deleteFavourites(favCountry)
//                Log.i("Favourites", "Deleted: ${favCountry.city}")
//                loadFavourites()
//            } catch (ex: Exception) {
//                _allFavourites.value = Response.Failure(ex)
//            }
//        }
//    }
//}