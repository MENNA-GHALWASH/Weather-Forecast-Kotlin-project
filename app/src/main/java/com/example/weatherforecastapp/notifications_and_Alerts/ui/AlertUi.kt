package com.example.weatherforecastapp.notifications_and_Alerts.ui

import android.app.Application
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecastapp.notifications_and_Alerts.model.AlertType
import com.example.weatherforecastapp.notifications_and_Alerts.model.WeatherAlert
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.notifications_and_Alerts.model.AlertScheduler
import com.example.weatherforecastapp.viewmodel.WeatherAlertViewModel
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar



@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherAlertsUI(
    weatherAlertViewModel: WeatherAlertViewModel,
    locationsViewModel: LocationsViewModel,
) {
    val context = LocalContext.current
    val apiKey = stringResource(R.string.geocoding_api)

    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val searchResults by locationsViewModel.city_resp.collectAsState()
    val isLoading by locationsViewModel.isLoading.collectAsState()

    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedTime = remember { mutableStateOf(LocalTime.now()) }
    val alarmType = remember { mutableStateOf("Notification") }
    val duration = remember { mutableStateOf(10) } // Duration in minutes
    var latLong by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Fetch cities when search query changes
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            locationsViewModel.getCities(searchQuery, apiKey)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Outlined Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { query -> searchQuery = query },
                onSearch = { active = false },
                active = active,
                onActiveChange = { active = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Search Location") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (active) {
                        Icon(
                            modifier = Modifier.clickable {
                                if (searchQuery.isNotEmpty()) searchQuery = "" else active = false
                            },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear"
                        )
                    }
                }
            ) {
                if (searchQuery.isNotEmpty()) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        )
                    } else {
                        LazyColumn {
                            items(searchResults) { result ->
                                Text(
                                    text = "${result.name}, ${result.country}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            latLong = LatLng(result.lat, result.lon)
                                            searchQuery = "${result.name}, ${result.country}"
                                            active = false
                                        }
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Date Picker
            Button(onClick = { showDatePicker(context, selectedDate) }) {
                Text("Select Date: ${selectedDate.value}")
            }

            // Time Picker
            Button(onClick = { showTimePicker(context, selectedTime) }) {
                Text("Select Time: ${selectedTime.value}")
            }

            // Alarm Type Selection
            Column {
                Text("Alarm Type:")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = alarmType.value == "Notification",
                        onClick = { alarmType.value = "Notification" }
                    )
                    Text("Notification")

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = alarmType.value == "Alarm",
                        onClick = { alarmType.value = "Alarm" }
                    )
                    Text("Alarm Sound")
                }
            }

            // Duration Slider
            DurationSlider(duration)

            // Set Alert Button
            Button(
                onClick = {
                    if (latLong.latitude != 0.0 && latLong.longitude != 0.0) {
                        val alert = WeatherAlert(
                            duration = duration.value * 60 * 1000L,
                            type = if (alarmType.value == "Notification") AlertType.NOTIFICATION else AlertType.ALARM,
                            latitude = latLong.latitude,
                            longitude = latLong.longitude,
                            date = selectedDate.value,
                            time = selectedTime.value
                        )

                        weatherAlertViewModel.addAlert(alert, context)
                        AlertScheduler.scheduleAlert(context, alert.id, alert.duration)

                        // Show Snackbar instead of Toast
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Alert for ${alert.date} is set")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Set Alert")
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun showDatePicker(context: Context, selectedDate: MutableState<LocalDate>) {
    val calendar = Calendar.getInstance()
    val datePicker = android.app.DatePickerDialog(
        context,
        { _, year, month, day ->
            selectedDate.value = LocalDate.of(year, month + 1, day)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePicker.show()
}

@RequiresApi(Build.VERSION_CODES.O)
fun showTimePicker(context: Context, selectedTime: MutableState<LocalTime>) {
    val calendar = Calendar.getInstance()
    val timePicker = TimePickerDialog(
        context,
        { _, hour, minute ->
            selectedTime.value = LocalTime.of(hour, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )
    timePicker.show()
}

@Composable
fun DurationSlider(duration: MutableState<Int>) {
    Column {
        Text(text = "Duration: ${duration.value} mins", fontSize = 16.sp)

        Slider(
            value = duration.value.toFloat(),
            onValueChange = { duration.value = it.toInt() },
            valueRange = 1f..60f,
            steps = 58,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
