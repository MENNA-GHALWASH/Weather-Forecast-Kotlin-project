//package com.example.weatherforecastapp.favourites.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import androidx.lifecycle.viewmodel.CreationExtras
//import com.example.weatherforecastapp.Data.CityResponse
//import com.example.weatherforecastapp.Data.FavouritesResp
//import com.example.weatherforecastapp.favourites.model.FavouritesRepo
//import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.launch
//
//class FavouritesViewModel(var repo: FavouritesRepo):ViewModel() {
//    //call repo's retrieve , delete and add
//
//    val allFavourites: Flow<List<FavouritesResp>> = repo.LoadFavourites()
//
//    fun addFavourite(favCountry:FavouritesResp){
//        viewModelScope.launch {
//            repo.addFavourite(favCountry)
//        }
//    }
//
//    fun deleteFavourite(favCountry: FavouritesResp){
//        viewModelScope.launch {
//            repo.deleteFavourite(favCountry)
//        }
//    }
//    //add coroutines
//}
//
//class FavouritesViewModelFactory(val repo:FavouritesRepo):ViewModelProvider.Factory{
//
//    override fun <T : ViewModel> create(
//        modelClass: Class<T>,
//        extras: CreationExtras
//    ): T {
//        if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)) {
//            return FavouritesViewModel(repo) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}