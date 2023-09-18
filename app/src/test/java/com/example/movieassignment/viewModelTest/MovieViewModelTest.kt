package com.example.movieassignment.viewModelTest

import MovieViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movieassignment.data.models.Movie
import com.example.movieassignment.data.repositories.MovieRepository
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MovieViewModelTest {
    @Mock private lateinit var movieRepository: MovieRepository

    private lateinit var viewModel: MovieViewModel
    private val QUERY_STRING = "Sample"
    private val MOVIE_FOUND = "Movie Found"
    private val PAGE_TITLE = "Romantic Movie"
    private val NO_NOVIES_FOUND_FOR_THE_QUERY = "No movies found for the query."
    private val allMovies = listOf(Movie("Sample Movie", "poster-image1"),
        Movie("Another Movie", "poster-image2"))

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = MovieViewModel(movieRepository)
    }

    @Test
    fun testPageTitle() {
        whenever(movieRepository.getTitle()).thenReturn(PAGE_TITLE)
        val result = viewModel.fetchPageTitle()

        Assert.assertEquals(PAGE_TITLE, result)
    }

    @Test
    fun `test If More Data Is Available`() {
        whenever(movieRepository.hasMoreData()).thenReturn(true)
        assert(viewModel.hasMoreDate())
    }

    @Test
    fun `test fetch initial movies loads data successfully` () {
        whenever(movieRepository.fetchMoviesFromAsset()).thenReturn(allMovies)

        viewModel.fetchInitialMovies()

        val result = viewModel.movieToShow.value
        Assert.assertNotNull(result)
        Assert.assertEquals(2,result?.size)
        Assert.assertEquals("Sample Movie", result?.get(0)?.name)
    }


    @Test
    fun `test searchQuery with valid query returns matching movies`() {
        whenever(movieRepository.searchMovie(QUERY_STRING)).thenReturn(allMovies)
        viewModel.setSearchQuery(QUERY_STRING)

        val result = viewModel.movieToShow.value
        Assert.assertEquals(allMovies, result)
    }
    
    @Test
    fun `test feedback message with empty movie list returns required string`() {
        whenever(movieRepository.searchMovie(QUERY_STRING)).thenReturn(emptyList())
        viewModel.setSearchQuery(QUERY_STRING)
        val resultWithEmptyList = viewModel.feedbackMessage.value
        Assert.assertEquals(NO_NOVIES_FOUND_FOR_THE_QUERY, resultWithEmptyList)
    }

    @Test
    fun `test feedback message with movie list returns required string`() {
        whenever(movieRepository.searchMovie(QUERY_STRING)).thenReturn(allMovies)
        viewModel.setSearchQuery(QUERY_STRING)
        val resultWithMovieData = viewModel.feedbackMessage.value
        Assert.assertEquals(MOVIE_FOUND, resultWithMovieData)

    }
}
