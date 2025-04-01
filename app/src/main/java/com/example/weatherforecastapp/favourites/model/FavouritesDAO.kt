package com.example.weatherforecastapp.favourites.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDAO {

    @Query("Select * from Favourites")
    fun getAllFavouriteCities(): Flow<List<FavClass>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavouriteCity(city: FavClass): Long

    @Delete
    suspend fun deleteFavouriteCity(city: FavClass): Int

}