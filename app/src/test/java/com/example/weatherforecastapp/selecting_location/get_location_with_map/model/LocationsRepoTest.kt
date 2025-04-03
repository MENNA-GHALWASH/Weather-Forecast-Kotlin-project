package com.example.weatherforecastapp.selecting_location.get_location_with_map.model

import com.example.weatherforecastapp.Data.CityResponse
import com.example.weatherforecastapp.Data.Remote.GeoCodingAPI
import com.example.weatherforecastapp.Data.Remote.RetrofitClient
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.LocationsRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit

class LocationsRepoTest {

    @Mock
    private lateinit var retrofitClient: RetrofitClient
    private lateinit var locationsRepo: LocationsRepo

    @Before
    fun setUp() {
        locationsRepo = LocationsRepo()
    }

    @Test
    fun `test fetchCities success`() = runBlocking {
        val place = "London"
        val apiKey = "test-api-key"
        val cityResponse = listOf(CityResponse("London", "UK", 51.5074, -0.1278))

        val geoCodingAPI = mock(GeoCodingAPI::class.java)
        `when`(geoCodingAPI.getCities(place, 5, apiKey)).thenReturn(cityResponse)

        val result = locationsRepo.fetchCities(place, apiKey)

        assertEquals(cityResponse, result)
    }

    @Test
    fun `test fetchCities failure`() = runBlocking {
        val place = "InvalidPlace"
        val apiKey = "test-api-key"

        `when`(retrofitClient.geo_coding_api.getCities(place, 5, apiKey)).thenThrow(Exception("Error fetching data"))

        val result = locationsRepo.fetchCities(place, apiKey)

        assertTrue(result.isEmpty())
    }
}
