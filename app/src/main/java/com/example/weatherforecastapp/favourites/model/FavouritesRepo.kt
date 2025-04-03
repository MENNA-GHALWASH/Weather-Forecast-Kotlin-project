package com.example.weatherforecastapp.favourites.model

import kotlinx.coroutines.flow.Flow


class FavouritesRepo(
    private val localDataSource: FavouritesLocalDataSource // ← Now uses data source
) {
    fun loadFavourites(): Flow<List<FavClass>> = localDataSource.getFavourites()

    suspend fun addFavourite(city: FavClass): Result<Long> {
        return localDataSource.addFavourite(city)
    }

    suspend fun  deleteFavourites(city: FavClass):Result<Int>{
        return localDataSource.deleteFavourite(city)
    }
}

