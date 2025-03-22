package com.example.weatherforecastapp.selecting_location.get_location_with_map.model

import com.example.weatherforecastapp.Data.Remote.RetrofitClient

class LocationsRepo {

    suspend fun fetchCities(place: String, apikey:String): List<String> {
        try {
            val geoCodingAPI = RetrofitClient.geo_coding_api
            val response = geoCodingAPI.getCities(/*String.contains place*/place, 5, apikey)
            return response.map { "${it.name}, ${it.country}" }

        } catch (e: Exception){
            return listOf("Error fetching data")
        }
    }


}