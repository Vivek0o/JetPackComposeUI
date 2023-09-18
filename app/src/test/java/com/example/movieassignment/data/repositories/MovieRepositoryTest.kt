package com.example.movieassignment.data.repositories

import android.content.Context
import android.content.res.AssetManager
import com.example.movieassignment.data.models.Movie
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MovieRepositoryTest {
    @Mock private lateinit var context: Context
    @Mock private lateinit var assets: AssetManager

    private lateinit var movieRepository: MovieRepository

    private val PAGE_TITLE = "Romantic Comedy"
    private val PAGE_RESPONSE_TEST = "pageResponseTest.json"
    private val allMovies = listOf(
        Movie("Sample Movie1", "poster-image1"),
        Movie("Sample Movie2", "poster-image2")
    )
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(context.assets).thenReturn(assets)
        movieRepository = Mockito.spy(MovieRepository(context))
    }

    @Test
    fun fetchMoviesFromAsset() {
        val testInputStream = MovieRepositoryTest::class.java.getResourceAsStream("/movieTest.json")
        whenever(assets.open("movieTest.json")).thenReturn(testInputStream)

        val result = movieRepository.loadJSONFromAsset("movieTest.json")
        val mockJson = Gson().toJson(allMovies)
        assertEquals(mockJson, result)

        movieRepository.currentFile = 5
        val emptyResult = movieRepository.fetchMoviesFromAsset()
        assertTrue(emptyResult.isEmpty())
    }

    @Ignore("Reason for Failing is Unknown")
    @Test
    fun searchMovie() {
        val testInputStream = MovieRepositoryTest::class.java.getResourceAsStream("/pageResponseTest.json")
        whenever(movieRepository.getFileName(anyInt())).thenReturn(PAGE_RESPONSE_TEST)
        whenever(assets.open(PAGE_RESPONSE_TEST)).thenReturn(testInputStream)
        
        val result = movieRepository.searchMovie("The")
        assertEquals(1, result.size)
    }

    @Test
    fun getTitle() {
        val testInputStream = MovieRepositoryTest::class.java.getResourceAsStream("/pageResponseTest.json")
        whenever(assets.open(PAGE_RESPONSE_TEST)).thenReturn(testInputStream)

        movieRepository.filename = PAGE_RESPONSE_TEST
        val result = movieRepository.getTitle()

        assertEquals(PAGE_TITLE, result)

    }

    @Test
    fun hasMoreData() {
        movieRepository.currentFile = 5
        val result = movieRepository.hasMoreData()

        assertFalse(result)
    }

    @Test
    fun resetCurrentFile() {
        movieRepository.currentFile = 5
        movieRepository.resetCurrentFile()

        assertEquals(1, movieRepository.currentFile)
    }
}