package com.example.weatherforecastapp.favourites.model

import kotlinx.coroutines.flow.Flow

class FavouritesRepo(private val itemDao: FavouritesDAO) {
    //save data
    //retrieve data
    //delete data
    fun LoadFavourites(): Flow<List<FavClass>> {
        return itemDao.getAllFavouriteCities()
    }

    suspend fun addFavourite(favouriteCity: FavClass): Long {
        return itemDao.insertFavouriteCity(favouriteCity)
    }

    suspend fun deleteFavourite(favouriteCity:FavClass): Int {
        return itemDao.deleteFavouriteCity(favouriteCity)
    }
}

