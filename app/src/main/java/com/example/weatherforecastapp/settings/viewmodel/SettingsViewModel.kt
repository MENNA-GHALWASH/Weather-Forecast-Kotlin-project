package com.example.weatherforecastapp.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weatherforecastapp.settings.model.Language
import com.example.weatherforecastapp.settings.model.LocationOption
import com.example.weatherforecastapp.settings.model.SettingsState
import com.example.weatherforecastapp.settings.model.TemperatureUnit
import com.example.weatherforecastapp.settings.model.WindSpeedUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState

    fun updateLocationOption(option: LocationOption) {
        _settingsState.value = _settingsState.value.copy(locationOption = option)
    }

    fun updateTemperatureUnit(unit: TemperatureUnit) {
        _settingsState.value = _settingsState.value.copy(temperatureUnit = unit)
    }

    fun updateWindSpeedUnit(unit: WindSpeedUnit) {
        _settingsState.value = _settingsState.value.copy(windSpeedUnit = unit)
    }

    fun updateLanguage(language: Language) {
        _settingsState.value = _settingsState.value.copy(language = language)
    }
}