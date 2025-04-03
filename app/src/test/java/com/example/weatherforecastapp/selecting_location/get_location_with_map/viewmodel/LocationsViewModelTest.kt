package com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.LocationsRepo
import com.example.weatherforecastapp.Data.CityResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class LocationsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepo: LocationsRepo

    private lateinit var viewModel: LocationsViewModel

    @Before
    fun setUp() {
        viewModel = LocationsViewModel(mockRepo)
    }

    @Test
    fun `test searchCities success`() = runBlockingTest {
        val place = "London"
        val apiKey = "test-api-key"
        val cities = listOf("London, UK, 51.5074, -0.1278")

        `when`(mockRepo.fetchCitiesStrings(place, apiKey)).thenReturn(cities) //explain

        viewModel.searchCities(place, apiKey)

        assertEquals(cities, viewModel.searchResults.value)
    }

    @Test
    fun `test getCities success`() = runBlockingTest {
        val place = "London"
        val apiKey = "test-api-key"
        val cityResponse = listOf(CityResponse("London", "UK", 51.5074, -0.1278))

        `when`(mockRepo.fetchCities(place, apiKey)).thenReturn(cityResponse)

        viewModel.getCities(place, apiKey)

        assertEquals(cityResponse, viewModel.city_resp.value)
    }

    @Test
    fun `test searchCities failure`() = runBlockingTest {
        val place = "InvalidPlace"
        val apiKey = "test-api-key"

        `when`(mockRepo.fetchCitiesStrings(place, apiKey)).thenThrow(Exception("Error fetching data"))

        viewModel.searchCities(place, apiKey)

        assertEquals(listOf("Error: Error fetching data"), viewModel.searchResults.value)
    }


    @Test
    fun `test loading state change during fetch`() = runBlockingTest {
        val place = "London"
        val apiKey = "test-api-key"

        `when`(mockRepo.fetchCitiesStrings(place, apiKey)).thenReturn(listOf("London, UK, 51.5074, -0.1278"))

        assertFalse(viewModel.isLoading.value)

        viewModel.searchCities(place, apiKey)

        assertTrue(viewModel.isLoading.value)

        assertFalse(viewModel.isLoading.value)
    }
}
