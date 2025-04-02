package com.example.weatherforecastapp.notifications_and_Alerts.model

import java.time.LocalDate
import java.time.LocalTime


data class WeatherAlert(
    val id: Long = 0,
    val duration: Long, // Duration in milliseconds
    val type: AlertType,
    val isActive: Boolean = true,
    val latitude: Double,
    val longitude: Double,
    val date: LocalDate,
    val time: LocalTime
)

enum class AlertType {
    NOTIFICATION,
    ALARM
}


