//package com.example.weatherforecastapp.Data
//
//import androidx.room.TypeConverter
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.example.weatherforecastapp.Data.CityResponse
//import com.example.weatherforecastapp.Data.WeatherResponse
//
//class Converters {
//    private val gson = Gson()
//
//    @TypeConverter
//    fun fromCityResponse(city: CityResponse): String {
//        return gson.toJson(city)
//    }
//
//    @TypeConverter
//    fun toCityResponse(cityString: String): CityResponse {
//        val type = object : TypeToken<CityResponse>() {}.type
//        return gson.fromJson(cityString, type)
//    }
//
//    @TypeConverter
//    fun fromWeatherResponse(weather: WeatherResponse): String {
//        return gson.toJson(weather)
//    }
//
//    @TypeConverter
//    fun toWeatherResponse(weatherString: String): WeatherResponse {
//        val type = object : TypeToken<WeatherResponse>() {}.type
//        return gson.fromJson(weatherString, type)
//    }
//
//    //add type converter to hourly and daily forecast
//
//    @TypeConverter
//    fun fromHourlyResponse(weather: HourlyForecast): String {
//        return gson.toJson(weather)
//    }
//
//    @TypeConverter
//    fun toHourlyResponse(weatherString: String): HourlyForecast {
//        val type = object : TypeToken<HourlyForecast>() {}.type
//        return gson.fromJson(weatherString, type)
//    }
//
//    @TypeConverter
//    fun fromDailResponse(weather: DailyForecast): String {
//        return gson.toJson(weather)
//    }
//
//    @TypeConverter
//    fun toDailyResponse(weatherString: String): DailyForecast {
//        val type = object : TypeToken<DailyForecast>() {}.type
//        return gson.fromJson(weatherString, type)
//    }
//}
