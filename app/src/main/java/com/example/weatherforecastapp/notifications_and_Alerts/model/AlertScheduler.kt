package com.example.weatherforecastapp.notifications_and_Alerts.model

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.BroadcastReceiver
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.home.daily_forecast.model.WeatherDatabase
import com.example.weatherforecastapp.home.daily_forecast.model.WeatherRepo
import com.example.weatherforecastapp.notifications_and_Alerts.utils.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object AlertScheduler {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleAlert(context: Context, alertId: Long, duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestExactAlarmPermission(context)
        }

        val intent = Intent(context, AlertReceiver::class.java).apply {
            putExtra("alertId", alertId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, alertId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(AlarmManager::class.java)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + duration,
            pendingIntent
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun requestExactAlarmPermission(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = android.net.Uri.parse("package:${context.packageName}")
            }
            context.startActivity(intent)
        }
    }

    fun cancelAlert(context: Context, alertId: Long) {
        val intent = Intent(context, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, alertId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(AlarmManager::class.java)
        alarmManager.cancel(pendingIntent)
    }
}







class AlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {

            NotificationHelper.showNotification(it, "Your scheduled weather alert is active!")

            val alertId = intent?.getLongExtra("alertId", -1L) ?: return
            val latitude = intent.getDoubleExtra("latitude", 0.0)
            val longitude = intent.getDoubleExtra("longitude", 0.0)

            // Launch coroutine to fetch weather data
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val apiKey = getApi(it)

                    val dao = WeatherDatabase.getDatabase(it).weatherDao()
                    val repo = WeatherRepo(dao)

                    val weatherResponse = repo.getCurrentWeather(latitude, longitude, apiKey)

                    // Handle the weather data if needed
                    // You can store the weather data or use it in the notification
                    // For now, let's just show a notification with some dummy data
                    val weatherInfo = "Weather: ${weatherResponse?.hourly?.get(0)?.weather?.get(0)?.description}\n" +
                            "Temperature: ${weatherResponse?.hourly?.get(0)?.temp}°C"

                    withContext(Dispatchers.Main) {
                        NotificationHelper.showNotification(it, weatherInfo)
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        NotificationHelper.showNotification(it, "Failed to fetch weather data.")
                    }
                }
            }
        }
    }

    private fun getApi(context: Context): String {
        return context.getString(R.string.geocoding_api)
    }
}
