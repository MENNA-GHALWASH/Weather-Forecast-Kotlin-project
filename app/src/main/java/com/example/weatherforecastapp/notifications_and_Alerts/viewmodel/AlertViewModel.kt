package com.example.weatherforecastapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.notifications_and_Alerts.model.AlertScheduler
import com.example.weatherforecastapp.notifications_and_Alerts.model.AlertType
import com.example.weatherforecastapp.notifications_and_Alerts.model.WeatherAlert
import com.example.weatherforecastapp.repository.WeatherAlertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


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

    fun addAlert(alert: WeatherAlert,context: Context) {
        viewModelScope.launch {

            repository.addAlert(alert)

            if (alert.type == AlertType.NOTIFICATION) {
                AlertScheduler.scheduleAlert(context, alert.id, alert.duration)
            }

            fetchAlerts()
        }
    }

    fun removeAlert(alert: WeatherAlert, context: Context) {
        viewModelScope.launch {
            repository.removeAlert(alert)

            AlertScheduler.cancelAlert(context, alert.id)

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
