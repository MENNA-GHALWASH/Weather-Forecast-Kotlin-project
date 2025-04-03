package com.example.weatherforecastapp.favourites.model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

// New file: FavouritesLocalDataSource.kt
class FavouritesLocalDataSource(
    private val dao: FavouritesDAO,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun getFavourites(): Flow<List<FavClass>> = dao.getAllFavouriteCities()

    suspend fun addFavourite(city: FavClass): Result<Long> {
        return try {
            Result.success(dao.insertFavouriteCity(city))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFavourite(city: FavClass): Result<Int> {
        return try {
            Result.success(dao.deleteFavouriteCity(city))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}