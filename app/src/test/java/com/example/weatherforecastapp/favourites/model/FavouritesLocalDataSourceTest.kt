//package com.example.weatherforecastapp.favourites.model
//
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flowOf
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.filters.MediumTest
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import org.junit.After
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@OptIn(ExperimentalCoroutinesApi::class)
//@MediumTest // Integration test (DAO + LocalDataSource)
//@RunWith(AndroidJUnit4::class)
//class FavouritesLocalDataSourceTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var dataSource: FavouritesLocalDataSource
//    private lateinit var database: FavouritesDAOImpl
//    private val testDispatcher = StandardTestDispatcher()
//
//    @Before
//    fun setup() {
//        Dispatchers.setMain(testDispatcher)
//
//        database = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            FavouritesDAOImpl::class.java
//        ).allowMainThreadQueries()
//            .build()
//
//        dataSource = FavouritesLocalDataSource(
//            dao = database.getFavouritesDAO(),
//            dispatcher = testDispatcher
//        )
//    }
//
//    @After
//    fun teardown() {
//        database.close()
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun getFavourites_shouldEmitEmptyListInitially() = runTest {
//        // When
//        val favourites = dataSource.getFavourites().first()
//
//        // Then
//        assertTrue(favourites.isEmpty())
//    }
//
//    @Test
//    fun addFavourite_shouldReturnSuccessWithRowId() = runTest {
//        // Given
//        val testCity = FavClass( 35.6762, 139.6503, "Tokyo",0)
//
//        // When
//        val result = dataSource.addFavourite(testCity)
//
//        // Then
//        assertTrue(result.isSuccess)
//        assertEquals(1L, result.getOrNull())
//    }
//
//    @Test
//    fun addFavourite_shouldActuallyStoreData() = runTest {
//        // Given
//        val testCity = FavClass(51.5074, -0.1278, "London",0 )
//
//        // When
//        dataSource.addFavourite(testCity)
//        val favourites = dataSource.getFavourites().first()
//
//        // Then
//        assertEquals(1, favourites.size)
//        assertEquals("London", favourites[0].city)
//    }
//
//    @Test
//    fun addFavourite_shouldReturnFailureOnError() = runTest {
//        // Arrange - Create a failing DAO (alternative approach)
//        val failingDao = object : FavouritesDAO {
//            override fun getAllFavouriteCities(): Flow<List<FavClass>> = flowOf(emptyList())
//            override suspend fun insertFavouriteCity(city: FavClass): Long {
//                throw Exception("Database error")
//            }
//            override suspend fun deleteFavouriteCity(city: FavClass): Int = 0
//        }
//
//        val errorDataSource = FavouritesLocalDataSource(failingDao)
//
//        // Act
//        val result = errorDataSource.addFavourite(FavClass( 48.8566, 2.3522, "Paris",0))
//
//        // Assert
//        assertTrue(result.isFailure)
//        assertEquals("Database error", result.exceptionOrNull()?.message)
//    }
//
//    @Test
//    fun deleteFavourite_shouldReturnSuccessWithRowsAffected() = runTest {
//        // Given
//        val testCity = FavClass(52.5200, 13.4050, "Berlin", 0)
//        dataSource.addFavourite(testCity)
//
//        // When
//        val result = dataSource.deleteFavourite(testCity)
//
//        // Then
//        assertTrue(result.isSuccess)
//        assertEquals(1, result.getOrNull())
//    }
//
//    @Test
//    fun deleteFavourite_shouldActuallyRemoveData() = runTest {
//        // Given
//        val testCity = FavClass( 40.7128, -74.0060, "New York",0)
//        dataSource.addFavourite(testCity)
//
//        // When
//        dataSource.deleteFavourite(testCity)
//        val favourites = dataSource.getFavourites().first()
//
//        // Then
//        assertTrue(favourites.isEmpty())
//    }
//
//    @Test
//    fun deleteFavourite_shouldReturnFailureOnError() = runTest {
//        // Arrange - Create a failing DAO
//        val failingDao = object : FavouritesDAO {
//            override fun getAllFavouriteCities(): Flow<List<FavClass>> = flowOf(emptyList())
//            override suspend fun insertFavouriteCity(city: FavClass): Long = 1L
//            override suspend fun deleteFavouriteCity(city: FavClass): Int {
//                throw Exception("Deletion error")
//            }
//        }
//
//        val errorDataSource = FavouritesLocalDataSource(failingDao)
//      //  val testCity = FavClass(0, "Rome", 41.9028, 12.4964)
//        val testCity = FavClass(41.9028, 12.4964, "Rome", 0)
//
//        // Act
//        val result = errorDataSource.deleteFavourite(testCity)
//
//        // Assert
//        assertTrue(result.isFailure)
//        assertEquals("Deletion error", result.exceptionOrNull()?.message)
//    }
//}