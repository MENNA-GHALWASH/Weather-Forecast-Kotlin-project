package com.example.weatherforecastapp.Data
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>
)

@Serializable
data class HourlyForecast(
    val dt: Long,
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val weather: List<WeatherDescription>
)

@Serializable
data class DailyForecast(
    val dt: Long,
    val temp: Temperature,
    @SerializedName("feels_like") val feelsLike: Temperature,
    val pressure: Int,
    val humidity: Int,
    val weather: List<WeatherDescription>
)

@Serializable
data class Temperature(
    val day: Double,
    val night: Double
)

@Serializable
data class WeatherDescription(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class CityResponse(val name: String, val country: String,val lat: Double, val lon: Double)
