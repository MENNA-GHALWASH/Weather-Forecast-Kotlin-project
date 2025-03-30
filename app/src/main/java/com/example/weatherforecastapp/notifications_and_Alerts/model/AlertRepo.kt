package com.example.weatherforecastapp.repository

import com.example.weatherforecastapp.notifications_and_Alerts.model.WeatherAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherAlertRepository {

    private val alerts = mutableListOf<WeatherAlert>()

    fun getAllAlerts(): Flow<List<WeatherAlert>> = flow {
        emit(alerts)
    }

    suspend fun addAlert(alert: WeatherAlert) {
        alerts.add(alert)
    }

    suspend fun removeAlert(alert: WeatherAlert) {
        alerts.remove(alert)
    }

    suspend fun updateAlert(alert: WeatherAlert) {
        val index = alerts.indexOfFirst { it.id == alert.id }
        if (index != -1) {
            alerts[index] = alert
        }
    }
}