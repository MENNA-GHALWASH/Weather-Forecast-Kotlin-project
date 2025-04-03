//package com.example.weatherforecastapp.home.daily_forecast.model
//
//import com.example.weatherforecastapp.Data.Remote.RetrofitClient
//import com.example.weatherforecastapp.Data.WeatherResponse
//import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.UNITS
//import com.example.weatherforecastapp.settings.model.Language
//import com.example.weatherforecastapp.settings.model.SettingsManager
//import com.example.weatherforecastapp.settings.model.TemperatureUnit
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.runBlocking
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Test
//import org.mockito.Mock
//import org.mockito.Mockito.*
//import org.mockito.MockitoAnnotations
//
//@ExperimentalCoroutinesApi
//class WeatherRepoTest {
//
//    @Mock
//    private lateinit var mockWeatherDao: WeatherDao
//
//    @Mock
//    private lateinit var mockRetrofitClient: RetrofitClient
//
//    @Mock
//    private lateinit var mockSettingsManager: SettingsManager
//
//    private lateinit var weatherRepo: WeatherRepo
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.openMocks(this)
//        val context= Context
//        weatherRepo = WeatherRepo(mockWeatherDao,context)
//    }
//
//    @Test
//    fun `getCurrentWeather should return network response when successful`() = runBlocking {
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
//        `when`(SettingsManager.getTemperatureUnit()).thenReturn(TemperatureUnit.Celsius)
//        `when`(SettingsManager.getLanguage()).thenReturn(Language.English)
//        `when`(RetrofitClient.one_call_api.getCurrentWeather(
//            lat, lon, "current", apiKey, "metric", "en"
//        )).thenReturn(mockResponse)
//
//        // Act
//        val result = weatherRepo.getCurrentWeather(lat, lon, apiKey)
//
//        // Assert
//        assertEquals(mockResponse, result)
//        verify(mockWeatherDao).insertWeatherResponse(mockResponse)
//    }
//
//    @Test
//    fun `getCurrentWeather should return cached response when network fails`(): Unit = runBlocking {
//        // Arrange
//        val lat = 37.7749
//        val lon = -122.4194
//        val apiKey = "test_api_key"
//        val mockCachedResponse = WeatherResponse(
//            lat = lat, lon = lon,
//            timezone = TODO(),
//            hourly = TODO(),
//            daily = TODO()
//        )
//
//        `when`(SettingsManager.getTemperatureUnit()).thenReturn(TemperatureUnit.Celsius)
//        `when`(SettingsManager.getLanguage()).thenReturn(Language.English)
//        `when`(RetrofitClient.one_call_api.getCurrentWeather(
//            anyDouble(), anyDouble(), anyString(), anyString(), anyString(), anyString()
//        )).thenThrow(RuntimeException("Network error"))
//        `when`(mockWeatherDao.getWeatherResponse(lat, lon)).thenReturn(mockCachedResponse)
//
//        // Act
//        val result = weatherRepo.getCurrentWeather(lat, lon, apiKey)
//
//        // Assert
//        assertEquals(mockCachedResponse, result)
//        verify(mockWeatherDao).getWeatherResponse(lat, lon)
//    }
//}