package com.example.weatherforecastapp.settings.model

data class SettingsState(
    val locationOption: LocationOption = LocationOption.GPS,
    val temperatureUnit: TemperatureUnit = TemperatureUnit.Celsius,
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.MeterPerSecond,
    val language: Language = Language.English
)

enum class LocationOption {
    GPS, MAP
}

enum class TemperatureUnit {
    Kelvin, Celsius, Fahrenheit
}

enum class WindSpeedUnit {
    MeterPerSecond, MilesPerHour
}

enum class Language {
    Arabic, English
}

