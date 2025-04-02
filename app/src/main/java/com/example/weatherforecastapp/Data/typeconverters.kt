package com.example.weatherforecastapp.Data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun fromWeatherDescriptionList(value: List<WeatherDescription>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toWeatherDescriptionList(value: String?): List<WeatherDescription>? {
        val listType: Type = object : TypeToken<List<WeatherDescription>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromHourlyForecastList(value: List<HourlyForecast>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toHourlyForecastList(value: String?): List<HourlyForecast>? {
        val listType: Type = object : TypeToken<List<HourlyForecast>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromDailyForecastList(value: List<DailyForecast>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toDailyForecastList(value: String?): List<DailyForecast>? {
        val listType: Type = object : TypeToken<List<DailyForecast>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromTemperature(value: Temperature?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toTemperature(value: String?): Temperature? {
        return Gson().fromJson(value, Temperature::class.java)
    }
}