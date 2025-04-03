package com.example.weatherforecastapp.favourites.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//import app.cash.turbine.test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

import com.example.weatherforecastapp.favourites.model.FavClass
import com.example.weatherforecastapp.favourites.model.FavouritesDAO
//import com.example.weatherforecastapp.database.AppDatabase

@RunWith(AndroidJUnit4::class)
class FavouritesDAOTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: FavouritesDAO

    private val testCity1 = FavClass(30.033333, 31.233334, "Cairo", 1)
    private val testCity2 = FavClass(40.7128, -74.0060, "New York", 2)

    @Before
    fun setup() {
        // Create in-memory database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.favouritesDAO()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllFavouriteCities_emptyDb_returnsEmptyList() = runTest {
        // When
        val result = dao.getAllFavouriteCities()

        // Then
        result.test {
            assertEquals(emptyList<FavClass>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insertFavouriteCity_insertsSuccessfully() = runTest {
        // When
        val insertedId = dao.insertFavouriteCity(testCity1)

        // Then
        assertTrue(insertedId > 0)

        // Verify by querying
        dao.getAllFavouriteCities().test {
            assertEquals(listOf(testCity1), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insertFavouriteCity_duplicate_ignoresConflict() = runTest {
        // Given
        dao.insertFavouriteCity(testCity1)

        // When (try to insert same city again)
        val result = dao.insertFavouriteCity(testCity1)

        // Then (should return -1 due to IGNORE conflict strategy)
        assertEquals(-1L, result)

        // Verify only one entry exists
        dao.getAllFavouriteCities().test {
            assertEquals(1, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteFavouriteCity_removesFromDatabase() = runTest {
        // Given
        dao.insertFavouriteCity(testCity1)
        dao.insertFavouriteCity(testCity2)

        // When
        val deletedCount = dao.deleteFavouriteCity(testCity1)

        // Then
        assertEquals(1, deletedCount)

        // Verify remaining cities
        dao.getAllFavouriteCities().test {
            assertEquals(listOf(testCity2), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteFavouriteCity_nonExistent_returnsZero() = runTest {
        // When (try to delete city that was never inserted)
        val deletedCount = dao.deleteFavouriteCity(testCity1)

        // Then
        assertEquals(0, deletedCount)
    }
}
