package com.example.weatherforecastapp.selecting_location.get_location_with_map.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.LocationsRepo
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModelFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LocationsUI(){
////    val repo = LocationsRepo()
////    val viewModel: LocationsViewModel by viewModels {
////        LocationsViewModelFactory(repo)
////    }
//    //a search bar
//    //a google map
//    //a hidden component for search results that comes up when you start typing
//
//    var searchQuery by remember { mutableStateOf("") }
//    val searchResults = remember { mutableStateListOf<String>() }
//    val defaultLocation = LatLng(30.0444, 31.2357)
//
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
//    }
//
//    Column(modifier = Modifier.padding(15.dp),
//        verticalArrangement = Arrangement.spacedBy(20.dp),
//        horizontalAlignment = Alignment.CenterHorizontally)
//    {
//        SearchBar(
//            query = searchQuery,
//            onQueryChange = { query ->
//              //  viewModel.onSearchQueryChanged(query)
//            },
//            onSearch = {
//                // Handle search submission (optional)
//            },
//            active = false, // Set to true if you want the search bar to expand
//            onActiveChange = { /* Handle active state change */ },
//            modifier = Modifier.fillMaxWidth()
//        ){
//
//        }
//        //google maps
//        GoogleMap(
//            modifier = Modifier.fillMaxSize()
//                .weight(1f),cameraPositionState = cameraPositionState
//        )
//    }
//}