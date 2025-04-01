package com.example.weatherforecastapp.favourites.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecastapp.Data.WeatherResponse

@Entity(tableName = "Favourites")
data class FavClass(val lat: Double, val lon: Double,val city:String, @PrimaryKey(autoGenerate = true) var id: Int = 0)