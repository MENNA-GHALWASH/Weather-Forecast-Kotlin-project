//package com.example.weatherforecastapp.home.daily_forecast.viewmodel
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import com.example.weatherforecastapp.Data.WeatherResponse
//import com.example.weatherforecastapp.home.daily_forecast.model.WeatherRepo
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import org.junit.After
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.mockito.Mock
//import org.mockito.Mockito.verify
//import org.mockito.Mockito.`when`
//import org.mockito.MockitoAnnotations
//
//@ExperimentalCoroutinesApi
//class WeatherViewModelTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Mock
//    private lateinit var mockRepo: WeatherRepo
//
//    private lateinit var viewModel: WeatherViewModel
//    private val testDispatcher = StandardTestDispatcher()
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.openMocks(this)
//        Dispatchers.setMain(testDispatcher)
//        viewModel = WeatherViewModel(mockRepo)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `getCurrentWeather should update current_weather when successful`() = runTest {
//        // Arrange
//        val lat = 37.7749
//        val lon = -122.4194
//        val apiKey = "test_api_key"
//        val mockResponse = WeatherResponse(
//            lat = lat, lon = lon,
//            timezone = TODO(),
//            hourly = TODO(),
//            daily = TODO()
//        )
//
//        `when`(mockRepo.getCurrentWeather(lat, lon, apiKey)).thenReturn(mockResponse)
//
//        // Act
//        viewModel.getCurrentWeather(lat, lon, apiKey)
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Assert
//        assertEquals(mockResponse, viewModel.current_weather.value)
//        verify(mockRepo).getCurrentWeather(lat, lon, apiKey)
//    }
//
//    @Test
//    fun `getCurrentWeather should set current_weather to null when failed`() = runTest {
//        // Arrange
//        val lat = 37.7749
//        val lon = -122.4194
//        val apiKey = "test_api_key"
//
//        `when`(mockRepo.getCurrentWeather(lat, lon, apiKey)).thenThrow(RuntimeException("Test error"))
//
//        // Act
//        viewModel.getCurrentWeather(lat, lon, apiKey)
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Assert
//        assertNull(viewModel.current_weather.value)
//        verify(mockRepo).getCurrentWeather(lat, lon, apiKey)
//    }
//}