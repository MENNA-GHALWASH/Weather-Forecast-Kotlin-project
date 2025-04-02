package com.example.weatherforecastapp.start_screen.model

import android.content.Context

object LocationPrefs {
    private const val PREFS_NAME = "location_prefs"
    private const val KEY_LATITUDE = "latitude"
    private const val KEY_LONGITUDE = "longitude"
    private const val KEY_CITY_NAME = "city_name"

    fun saveLocation(context: Context, lat: Double, lon: Double) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().apply {
            putFloat(KEY_LATITUDE, lat.toFloat())
            putFloat(KEY_LONGITUDE, lon.toFloat())
            apply()
        }
    }

    fun saveCityName(context: Context, cityName: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().apply {
            putString(KEY_CITY_NAME, cityName)
            apply()
        }
    }

    fun getLocation(context: Context): Pair<Double, Double>? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return if (prefs.contains(KEY_LATITUDE) && prefs.contains(KEY_LONGITUDE)) {
            Pair(
                prefs.getFloat(KEY_LATITUDE, 0f).toDouble(),
                prefs.getFloat(KEY_LONGITUDE, 0f).toDouble()
            )
        } else {
            null
        }
    }

    fun getCityName(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_CITY_NAME, null)
    }

    fun getLastKnownLocationLatLong(context: Context): Pair<Double, Double>? {
        val lastLocation = LocationPrefs.getLocation(context)
        return lastLocation
    }
//use this for navigating to weather

    fun getLastKnownCity(context: Context): String? {
        val lastCityName = LocationPrefs.getCityName(context)
        return lastCityName
    }
}

//fun getLastKnownLocationLatLong(context: Context): Pair<Double, Double>? {
//    val lastLocation = LocationPrefs.getLocation(context)
//    return lastLocation
//}
////use this for navigating to weather
//
//fun getLastKnownCity(context: Context): String? {
//    val lastCityName = LocationPrefs.getCityName(context)
//    return lastCityName
//}
//
