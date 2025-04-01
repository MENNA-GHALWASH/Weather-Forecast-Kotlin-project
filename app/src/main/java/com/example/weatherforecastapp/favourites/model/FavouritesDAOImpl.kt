package com.example.weatherforecastapp.favourites.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [FavClass::class], version = 3, exportSchema = false)

abstract class FavouritesDAOImpl : RoomDatabase() {
    abstract fun getFavouritesDAO(): FavouritesDAO

    companion object {
        @Volatile
        private var instance: FavouritesDAOImpl? = null

        fun getInstance(context: Context): FavouritesDAOImpl {
            return instance ?: synchronized(this) {
                val INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    FavouritesDAOImpl::class.java,
                    "Favourites"
                )    .fallbackToDestructiveMigration() // 💥 WARNING: This will DELETE existing data!
                    .build()
                instance = INSTANCE
                INSTANCE
            }
        }
    }
}