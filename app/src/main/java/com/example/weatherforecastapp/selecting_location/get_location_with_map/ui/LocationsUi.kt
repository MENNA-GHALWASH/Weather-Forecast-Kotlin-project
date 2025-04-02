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
import androidx.compose.ui.util.trace

import com.example.weatherforecastapp.Data.WeatherResponse
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.favourites.viewmodel.FavouritesViewModel
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsUI(
    viewModel: LocationsViewModel,
    goToWeather: (/*WeatherResponse?*/lat:Double,lon:Double,String) -> Unit ,
    favouriteViewModel: FavouritesViewModel,
//    fromFav: Boolean

    ) {
    val apiKey = stringResource(R.string.geocoding_api)

    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val searchResults by viewModel.city_resp.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
   // val currentWeather by viewModel.current_weather.collectAsState()

    var defaultLocation by remember { mutableStateOf(LatLng(30.0444, 31.2357)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    var latLong by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val currentLatLong by rememberUpdatedState(latLong)

    // Fetch cities based on search query
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            viewModel.getCities(searchQuery, apiKey)
        }
    }

//    LaunchedEffect(latLong) {
//        if ((latLong.latitude!=0.0)&&(latLong.longitude!=0.0)) {
//            viewModel.getCurrentWeather(currentLatLong.latitude, currentLatLong.longitude, apiKey)
//        }
//    }

    // Animate camera when location updates
    LaunchedEffect(defaultLocation) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f),
            durationMs = 1000
        )
    }

    Column(
        modifier = Modifier.padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { query -> searchQuery = query },
            onSearch = {
                //if (fromFav == false) {
                    if (currentLatLong.latitude != 0.0 && currentLatLong.longitude != 0.0) {
                      //  viewModel.getCurrentWeather(currentLatLong.latitude, currentLatLong.longitude, apiKey) //changes the value of viewmodel's current weather
                       // goToWeather(currentWeather/*send the search query*/,searchQuery)
                        //Log.i("fromFav", "LocationsUI: fromFav is $fromFav")
                        goToWeather(currentLatLong.latitude,currentLatLong.longitude,searchQuery) // should take double lat lon and city name
                    }
                    active = false
//                } else {
//                    // Save to Room DB and navigate back to favourites
//                    searchResults.firstOrNull { it.lat == currentLatLong.latitude && it.lon == currentLatLong.longitude }
//                        ?.let { favouriteViewModel.addToFavourites(it) }
//                    Log.i("fromFav", "LocationsUI: fromFav is $fromFav")
//                    goToWeather(null,searchQuery) //this must be the root source of the error
//
//                    active = false
//                    //for some reason the flag doesnt change
//                }
            },
            active = active,
            onActiveChange = { active = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search cities") },
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
                        val x = searchResults
//                        val x = viewModel.city_resp.value
                        Log.i("Cities from ui", "LocationsUI: $x")
                        items(x) { result ->
                            Text(
                                text = "${result.name}, ${result.country}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        defaultLocation = LatLng(result.lat, result.lon)
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

        // Google Map with marker
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            cameraPositionState = cameraPositionState,
            onMapClick = { loc -> latLong = loc
                Log.i("Locatiom from ui", "LocationsUI: selected city is $latLong")
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
//my hyp:
//the updated version doesnt display search results in the lazy column
//it doesn't update the value of latlong location/current location
//since default impl of fromFav is false, it seems to not be updated