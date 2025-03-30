package com.example.weatherforecastapp.Data
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>
)

data class HourlyForecast(
    val dt: Long,
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val weather: List<WeatherDescription>
)

data class DailyForecast(
    val dt: Long,
    val temp: Temperature,
    @SerializedName("feels_like") val feelsLike: Temperature,
    val pressure: Int,
    val humidity: Int,
    val weather: List<WeatherDescription>
)

data class Temperature(
    val day: Double,
    val night: Double
)

data class WeatherDescription(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

//not used in methods
@Entity(tableName = "Favourites")
data class CityResponse(
    val name: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)


