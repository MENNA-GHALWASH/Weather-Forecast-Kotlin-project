package com.example.weatherforecastapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.notifications_and_Alerts.model.AlertType
import com.example.weatherforecastapp.notifications_and_Alerts.model.WeatherAlert

import com.example.weatherforecastapp.repository.WeatherAlertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class WeatherAlertViewModel(private val repository: WeatherAlertRepository) : ViewModel() {

    private val _alerts = MutableStateFlow<List<WeatherAlert>>(emptyList())
    val alerts: StateFlow<List<WeatherAlert>> = _alerts

    init {
        fetchAlerts()
    }

    private fun fetchAlerts() {
        viewModelScope.launch {
            repository.getAllAlerts().collect { alerts ->
                _alerts.value = alerts
            }
        }
    }

    fun addAlert(duration: Long, type: AlertType) {
        viewModelScope.launch {
            val alert = WeatherAlert(
                id = UUID.randomUUID().mostSignificantBits,
                duration = duration,
                type = type
            )
            repository.addAlert(alert)
            fetchAlerts()
        }
    }

    fun removeAlert(alert: WeatherAlert) {
        viewModelScope.launch {
            repository.removeAlert(alert)
            fetchAlerts()
        }
    }

    fun updateAlert(alert: WeatherAlert) {
        viewModelScope.launch {
            repository.updateAlert(alert)
            fetchAlerts()
        }
    }
}