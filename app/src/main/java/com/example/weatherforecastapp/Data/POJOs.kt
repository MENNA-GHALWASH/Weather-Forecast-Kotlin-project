package com.example.weatherforecastapp.Data
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import androidx.room.TypeConverters

@Entity(tableName = "weather_response")
data class WeatherResponse(
    @PrimaryKey val lat: Double,
    val lon: Double,
    val timezone: String,
    @TypeConverters(Converters::class) val hourly: List<HourlyForecast>,
    @TypeConverters(Converters::class) val daily: List<DailyForecast>
)

@Entity(tableName = "hourly_forecast")
data class HourlyForecast(
    @PrimaryKey val dt: Long,
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    @TypeConverters(Converters::class) val weather: List<WeatherDescription>
)

@Entity(tableName = "daily_forecast")
data class DailyForecast(
    @PrimaryKey val dt: Long,
    @TypeConverters(Converters::class) val temp: Temperature,
    @SerializedName("feels_like") @TypeConverters(Converters::class) val feelsLike: Temperature,
    val pressure: Int,
    val humidity: Int,
    @TypeConverters(Converters::class) val weather: List<WeatherDescription>
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
data class LocationResponse(
    val name: String,
    val local_names: Map<String, String>?, // Nullable because not always needed
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String?
)


//not used in methods
data class CityResponse(
    val name: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
)


