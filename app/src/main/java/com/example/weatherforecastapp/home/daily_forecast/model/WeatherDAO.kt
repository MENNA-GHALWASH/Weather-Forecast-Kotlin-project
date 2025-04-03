package com.example.weatherforecastapp.home.daily_forecast.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.Data.DailyForecast
import com.example.weatherforecastapp.Data.HourlyForecast
import com.example.weatherforecastapp.Data.WeatherResponse

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)

    @Query("SELECT * FROM weather_response WHERE lat = :lat AND lon = :lon")
    suspend fun getWeatherResponse(lat: Double, lon: Double): WeatherResponse

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertHourlyForecast(hourlyForecast: List<HourlyForecast>)
//
//    @Query("SELECT * FROM hourly_forecast")
//    suspend fun getHourlyForecast(): List<HourlyForecast>?
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertDailyForecast(dailyForecast: List<DailyForecast>)
//
//    @Query("SELECT * FROM daily_forecast")
//    suspend fun getDailyForecast(): List<DailyForecast>?
}