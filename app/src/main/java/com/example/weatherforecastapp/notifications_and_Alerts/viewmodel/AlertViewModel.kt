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
import java.time.LocalDate
import java.time.LocalTime
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

    // Function to add an alert with necessary details like duration, type, latitude, and longitude
    fun addAlert(alert: WeatherAlert,context: Context) {
        viewModelScope.launch {

            // Add the alert to the repository
            repository.addAlert(alert)

            // If the alert type is Notification, schedule it using AlertScheduler
            if (alert.type == AlertType.NOTIFICATION) {
                AlertScheduler.scheduleAlert(context, alert.id, alert.duration)
            }

            // Refresh the list of alerts
            fetchAlerts()
        }
    }

    // Function to remove an alert (both from the repository and cancel the scheduled alert)
    fun removeAlert(alert: WeatherAlert, context: Context) {
        viewModelScope.launch {
            // Remove the alert from the repository
            repository.removeAlert(alert)

            // Cancel the scheduled alert from the AlertScheduler
            AlertScheduler.cancelAlert(context, alert.id)

            // Refresh the list of alerts
            fetchAlerts()
        }
    }

    // Function to update an alert (update the alert data in repository)
    fun updateAlert(alert: WeatherAlert) {
        viewModelScope.launch {
            // Update the alert in the repository
            repository.updateAlert(alert)

            // Refresh the list of alerts
            fetchAlerts()
        }
    }
}
