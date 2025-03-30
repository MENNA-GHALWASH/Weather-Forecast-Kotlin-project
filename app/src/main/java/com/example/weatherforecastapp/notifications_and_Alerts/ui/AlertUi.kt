package com.example.weatherforecastapp.notifications_and_Alerts.ui

import com.example.weatherforecastapp.notifications_and_Alerts.model.AlertType
import com.example.weatherforecastapp.notifications_and_Alerts.model.WeatherAlert


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherforecastapp.viewmodel.WeatherAlertViewModel
import kotlinx.coroutines.launch

@Composable
fun WeatherAlertsUI(viewModel: WeatherAlertViewModel) {
    val alerts by viewModel.alerts.collectAsState()

    var duration by remember { mutableStateOf(3600000L) } // Default 1 hour
    var alertType by remember { mutableStateOf(AlertType.NOTIFICATION) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Add Weather Alert Button
        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.addAlert(duration, alertType)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Weather Alert")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Duration Picker
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Duration (hours): ")
            Slider(
                value = (duration / 3600000).toFloat(),
                onValueChange = { duration = (it * 3600000).toLong() },
                valueRange = 1f..24f,
                steps = 23,
                modifier = Modifier.weight(1f)
            )
            Text("${(duration / 3600000).toInt()}h")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Alert Type Picker
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Alert Type: ")
            RadioButton(
                selected = alertType == AlertType.NOTIFICATION,
                onClick = { alertType = AlertType.NOTIFICATION }
            )
            Text("Notification")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = alertType == AlertType.ALARM,
                onClick = { alertType = AlertType.ALARM }
            )
            Text("Alarm")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Weather Alerts
        LazyColumn {
            items(alerts) { alert ->
                WeatherAlertItem(alert, viewModel)
            }
        }
    }
}

@Composable
fun WeatherAlertItem(alert: WeatherAlert, viewModel: WeatherAlertViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Alert Type: ${alert.type.name}")
            Text("Duration: ${alert.duration / 3600000} hours")
            Button(
                onClick = {
                    viewModel.removeAlert(alert)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Stop Alert")
            }
        }
    }
}