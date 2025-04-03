package com.example.weatherforecastapp.settings.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.util.Locale


import android.content.SharedPreferences

object SettingsManager {

    private const val PREF_NAME = "settings_pref"
    private const val LANGUAGE_KEY = "language_key"
    private const val LOCATION_OPTION_KEY = "location_option_key"
    private const val TEMPERATURE_UNIT_KEY = "temperature_unit_key"
    private const val WIND_SPEED_UNIT_KEY = "wind_speed_unit_key"

    fun setLanguage(context: Context, lang: Language) {
        saveToPreferences(context, LANGUAGE_KEY, lang.name)
    }

    fun getLanguage(context: Context): Language {
        val langName = loadFromPreferences(context, LANGUAGE_KEY, Language.English.name)
        return Language.valueOf(langName)
    }

    fun setLocationOption(context: Context, option: LocationOption) {
        saveToPreferences(context, LOCATION_OPTION_KEY, option.name)
    }

    fun getLocationOption(context: Context): LocationOption {
        val optionName = loadFromPreferences(context, LOCATION_OPTION_KEY, LocationOption.GPS.name)
        return LocationOption.valueOf(optionName)
    }

    fun setTemperatureUnit(context: Context, unit: TemperatureUnit) {
        saveToPreferences(context, TEMPERATURE_UNIT_KEY, unit.name)
    }

    fun getTemperatureUnit(context: Context): TemperatureUnit {
        val unitName = loadFromPreferences(context, TEMPERATURE_UNIT_KEY, TemperatureUnit.Celsius.name)
        return TemperatureUnit.valueOf(unitName)
    }

    fun setWindSpeedUnit(context: Context, unit: WindSpeedUnit) {
        saveToPreferences(context, WIND_SPEED_UNIT_KEY, unit.name)
    }

    fun getWindSpeedUnit(context: Context): WindSpeedUnit {
        val unitName = loadFromPreferences(context, WIND_SPEED_UNIT_KEY, WindSpeedUnit.MeterPerSecond.name)
        return WindSpeedUnit.valueOf(unitName)
    }

    private fun saveToPreferences(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun loadFromPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun setLocale(context: Context, language: Language) {
        val locale = when (language) {
            Language.English -> Locale("en")
            Language.Arabic -> Locale("ar")
        }

        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        // Restart activity to apply the new language
        restartActivity(context)
    }

    private fun restartActivity(context: Context) {
        val intent = (context as Activity).intent
        context.overridePendingTransition(0, 0)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        context.finish()
        context.overridePendingTransition(0, 0)
        context.startActivity(intent)
    }
}
