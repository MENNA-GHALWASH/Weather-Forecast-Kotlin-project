package com.example.weatherforecastapp.favourites.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecastapp.Data.CityResponse

@Database(entities = [CityResponse::class], version = 1)
abstract class FavouritesDAOImpl: RoomDatabase() {
    abstract fun getFavouritesDAO() : FavouritesDAO

    companion object{
        @Volatile
        private var instance: FavouritesDAOImpl? = null
        fun getInstance(context: Context): FavouritesDAOImpl {
            return instance ?: synchronized(this){
                val INSTANCE = Room.databaseBuilder(context, FavouritesDAOImpl::class.java, "roomdb").build()
                instance = INSTANCE
                INSTANCE
            }
        }
    }
}