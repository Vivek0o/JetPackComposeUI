package com.example.movieassignment.ui.views

import MovieViewModel
import MovieViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.movieassignment.data.repositories.MovieRepository
import com.example.movieassignment.ui.composable.MoviesScreen


class MainActivity : ComponentActivity() {

    private val movieRepository by lazy { MovieRepository(this) }
    private val viewModelFactory by lazy { MovieViewModelFactory(movieRepository) }

    private val viewModel by viewModels<MovieViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesScreen(viewModel)
        }
    }
}
