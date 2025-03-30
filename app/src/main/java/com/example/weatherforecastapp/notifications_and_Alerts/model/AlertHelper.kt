package com.example.weatherforecastapp.notifications_and_Alerts.model


data class WeatherAlert(
    val id: Long = 0,
    val duration: Long, // Duration in milliseconds
    val type: AlertType,
    val isActive: Boolean = true
)

enum class AlertType {
    NOTIFICATION,
    ALARM
}