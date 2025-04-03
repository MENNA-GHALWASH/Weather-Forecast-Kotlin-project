//package com.example.weatherforecastapp.favourites.model
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.filters.SmallTest
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.test.runTest
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.junit.Assert.*
//
//@RunWith(AndroidJUnit4::class)
//@SmallTest
//class FavouritesDAOTest {
//
//    private lateinit var database: FavouritesDAOImpl
//    private lateinit var dao: FavouritesDAO
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Before
//    fun setup() {
//        database = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            FavouritesDAOImpl::class.java
//        ).allowMainThreadQueries()
//            .build()
//        dao = database.getFavouritesDAO()
//    }
//
//    @Test
//    fun insertFavouriteCity_andRetrieve() = runTest {
//        val testCity = FavClass(35.6762, 139.6503, "Tokyo", 0)
//        dao.insertFavouriteCity(testCity)
//
//        val cities = dao.getAllFavouriteCities().first()
//
//        assertEquals(1, cities.size)
//        assertEquals("Tokyo", cities[0].city)
//        assertEquals(35.6762, cities[0].lat, 0.001)
//    }
//
//    @Test
//    fun deleteFavouriteCity_removesFromDatabase() = runTest {
//        val testCity = FavClass(51.5074, -0.1278, "London", 0)
//        val id = dao.insertFavouriteCity(testCity)
//
//        dao.deleteFavouriteCity(testCity.copy(id = id.toInt()))
//
//        assertTrue(dao.getAllFavouriteCities().first().isEmpty())
//    }
//
//    @Test
//    fun getAllFavouriteCities_emitsOnInsert() = runTest {
//        assertTrue(dao.getAllFavouriteCities().first().isEmpty())
//
//        val testCity = FavClass(52.5200, 13.4050, "Berlin", 0)
//        dao.insertFavouriteCity(testCity)
//
//        val updated = dao.getAllFavouriteCities().first()
//        assertEquals(1, updated.size)
//        assertEquals("Berlin", updated[0].city)
//    }
//
//    @After
//    fun teardown() {
//        database.close()
//    }
//}