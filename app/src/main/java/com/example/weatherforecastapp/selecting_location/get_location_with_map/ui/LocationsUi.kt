package com.example.weatherforecastapp.selecting_location.get_location_with_map.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LocationsUI(){
    //a search bar
    //a google map
    //a hidden component for search results that comes up when you start typing
    //need to seriously research its name
    Column(modifier = Modifier.padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        SearchBar() {

        }

        Google
    }
}