package com.example.weatherforecastapp.favourites.model

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking


@RunWith(AndroidJUnit4::class)
class FavouritesLocalDataSourceRealTest {
    private lateinit var localDataSource: FavouritesLocalDataSource
    private lateinit var database: FavouritesDAOImpl // Your Room database class
    private lateinit var dao: FavouritesDAO

    private val testCity1 = FavClass(30.033333, 31.233334, "Cairo", 1)
    private val testCity2 = FavClass(40.7128, -74.0060, "New York", 2)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FavouritesDAOImpl::class.java
        ).allowMainThreadQueries() // Allows running DB ops on main thread (for testing)
            .build()

        dao = database.getFavouritesDAO()
        localDataSource = FavouritesLocalDataSource(dao, testDispatcher)
    }

    @After
    fun tearDown() {
        database.close() // Clean up
    }

    @Test
    fun getFavourites_returnsEmptyFlowInitially() = runBlocking {
        // When

        val resultFlow = localDataSource.getFavourites()

        // Then
        val collectedItems = mutableListOf<List<FavClass>>()
        resultFlow.collect {
            collectedItems.add(it)
        }
        verify { dao.getAllFavouriteCities() }

    }

    @Test
    fun addFavourite_success_returnsSuccessResult() = runBlocking {
        // When
        val result = localDataSource.addFavourite(testCity1)

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()!! > 0) // Returns inserted row ID
    }

    @Test
    fun addFavourite_duplicate_ignoresDueToConflictStrategy() = runBlocking {
        // Given
        localDataSource.addFavourite(testCity1)

        // When (try to insert again)
        val result = localDataSource.addFavourite(testCity1)

        // Then (should return -1 due to IGNORE conflict strategy)
        assertEquals(-1L, result.getOrNull())
    }

    @Test
    fun deleteFavourite_success_returnsDeletedCount() = runBlocking {
        // Given
        localDataSource.addFavourite(testCity1)
        localDataSource.addFavourite(testCity2)

        // When
        val result = localDataSource.deleteFavourite(testCity1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()) // 1 row deleted
    }

    @Test
    fun deleteFavourite_nonExistent_returnsZero() = runBlocking {
        // When (try to delete non-existent city)
        val result = localDataSource.deleteFavourite(testCity1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrNull()) // 0 rows deleted
    }
}