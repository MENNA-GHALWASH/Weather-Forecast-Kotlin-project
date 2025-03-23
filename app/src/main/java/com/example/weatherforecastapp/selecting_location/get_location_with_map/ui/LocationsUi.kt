package com.example.weatherforecastapp.selecting_location.get_location_with_map.ui

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.LocationsRepo
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModelFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun LocationsUI(viewModel: LocationsViewModel) {

//code for circular p
//        CircularProgressIndicator(
//            modifier = Modifier.width(64.dp),
//            color = MaterialTheme.colorScheme.secondary,
//            trackColor = MaterialTheme.colorScheme.surfaceVariant,
//        )

    val apiKey = stringResource(R.string.geocoding_api)
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val searchResults by viewModel.searchResults.collectAsState() // Observe search results from ViewModel
    val isLoading by viewModel.isLoading.collectAsState() // Observe loading state


    val defaultLocation = LatLng(30.0444, 31.2357)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            viewModel.searchCities(searchQuery, apiKey)
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
                    Icon( modifier = Modifier.clickable{searchQuery = ""
                                                       active = false},
                        imageVector = Icons.Default.Close, contentDescription = "delete")
                }
            }
        ) {
            // Display search results
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
                                text = result,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { /* Handle result click */ }
                                    .padding(16.dp)
                            )
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
            cameraPositionState = cameraPositionState
        )
    }
}