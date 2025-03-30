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
//}
