package com.example.weatherforecastapp.selecting_location.get_location_with_map.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherforecastapp.Data.DailyForecast
import com.example.weatherforecastapp.Data.HourlyForecast
import com.example.weatherforecastapp.Data.WeatherResponse
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.LocationsRepo
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable

//go to favourites as optional with default value
fun LocationsUI(viewModel: LocationsViewModel,goToWeather:(WeatherResponse?/*, HourlyForecast?, DailyForecast?*/)->Unit = {_/*,_,_*/ ->}) {

    val apiKey = stringResource(R.string.geocoding_api)
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val currentWeather by viewModel.current_weather.collectAsState()


    var defaultLocation by remember { mutableStateOf(LatLng(30.0444, 31.2357)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    var latLong by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val currentLatLong by rememberUpdatedState(latLong)

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            viewModel.searchCities(searchQuery, apiKey)
        }
    }

    LaunchedEffect(defaultLocation) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f),
            durationMs = 1000
        )
    }

    LaunchedEffect(currentLatLong) {
        if (currentLatLong.latitude != 0.0 && currentLatLong.longitude != 0.0) {
            Log.d("WeatherFlow", "Fetching weather for ${currentLatLong.latitude},${currentLatLong.longitude}")
            viewModel.getCurrentWeather(
                currentLatLong.latitude,
                currentLatLong.longitude,
                apiKey
            ).also {
                Log.d("WeatherFlow", "Weather API call initiated")
            }
        }
    }


    Column(
        modifier = Modifier.padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { query ->
                searchQuery = query
            },
            onSearch = {
                if (currentLatLong.latitude != 0.0 && currentLatLong.longitude != 0.0) {
                    viewModel.getCurrentWeather(
                        currentLatLong.latitude,
                        currentLatLong.longitude,
                        apiKey
                    )

                    if (currentWeather != null) {
                        Log.i("Navigation", "Navigating with weather data")
                        goToWeather(currentWeather)
                        //need to pass it my string
                    } else {
                        Log.e("Navigation", "Weather data is null")
                    }
                }
                active = false
            },
            active = active,
            onActiveChange = {
                active = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Search cities")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
                if(active) {
                    Icon( modifier = Modifier.clickable{
                        if (searchQuery.isNotEmpty()){
                            searchQuery = ""
                        }
                        else active = false},
                        imageVector = Icons.Default.Close, contentDescription = "delete")
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
                            if (result!=null){
                                var resAr = result.split(",")

                                var lat = resAr[2].toDouble()
                                var lon = resAr[3].toDouble()

                                var res = resAr[0]+" , "+ resAr[1]

                                Text(
                                    text = res,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            //pass lat and lon to a method that changes camera position
                                            defaultLocation = LatLng(lat, lon)
                                            latLong = LatLng(lat, lon)
                                            searchQuery = res
                                            active = false

                                            //in here we get the item -> which is the city name,
                                            //then we pass it to a method which will:
                                            //1: get its latlong
                                            //2: call the onecallweather api
                                            //3: get the weather data
                                            //4: change the map's camera position
                                            //5: when we click on search, we will get navigated to the next screen
                                            //6: with the data already received
                                        }
                                        .padding(16.dp)
                                )
                            }

                        }
                    }
                }
            }
        }

        // Google Maps
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            cameraPositionState = cameraPositionState,
            onMapClick = { loc ->
                latLong = loc // Update latLong when user taps on the map
            }
        ) {
            Marker(
                state = MarkerState(position = latLong),
                title = "Selected Location",
                snippet = "Lat: ${latLong.latitude}, Lng: ${latLong.longitude}"
            )
        }


    }
}