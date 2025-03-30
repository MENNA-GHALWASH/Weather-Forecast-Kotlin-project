package com.example.weatherforecastapp.favourites.model

import com.example.weatherforecastapp.Data.CityResponse
import kotlinx.coroutines.flow.Flow

class FavouritesRepo(private val itemDao: FavouritesDAO) {
    //save data
    //retrieve data
    //delete data
    fun LoadFavourites(): Flow<List<CityResponse>> {
        return itemDao.getAllFavouriteCities()
    }

    suspend fun addFavourite(favouriteCity: CityResponse): Long {
        return itemDao.insertFavouriteCity(favouriteCity)
    }

    suspend fun deleteFavourite(favouriteCity:CityResponse): Int {
        return itemDao.deleteFavouriteCity(favouriteCity)
    }
}

