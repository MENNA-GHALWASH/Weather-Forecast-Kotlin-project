//package com.example.weatherforecastapp.favourites.model
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.weatherforecastapp.Data.FavouritesResp
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface FavouritesDAO {
//
//    @Query("Select * from Favourites")
//    fun getAllFavouriteCities(): Flow<List<FavouritesResp>>
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertFavouriteCity(city: FavouritesResp): Long
//
//    @Delete
//    suspend fun deleteFavouriteCity(city: FavouritesResp?): Int
//
//}