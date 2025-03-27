package com.example.weatherforecastapp.favourites.model

import com.example.weatherforecastapp.Data.CityResponse
import com.example.weatherforecastapp.Data.FavouritesResp
import kotlinx.coroutines.flow.Flow

class FavouritesRepo(private val itemDao: FavouritesDAO) {
    //save data
    //retrieve data
    //delete data
    fun LoadFavourites(): Flow<List<FavouritesResp>> {
        return itemDao.getAllFavouriteCities()
    }

    suspend fun addFavourite(favouriteCity: FavouritesResp): Long {
        return itemDao.insertFavouriteCity(favouriteCity)
    }

    suspend fun deleteFavourite(favouriteCity:FavouritesResp): Int {
        return itemDao.deleteFavouriteCity(favouriteCity)
    }
}