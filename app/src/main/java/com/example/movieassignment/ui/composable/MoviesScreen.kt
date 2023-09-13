package com.example.movieassignment.ui.composable

import MovieViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MoviesScreen(viewModel: MovieViewModel = viewModel()) {
    val moviesToShow by viewModel.movieToShow.observeAsState(emptyList())
    val queryString by viewModel.queryString.observeAsState(initial = "")
    val feedbackMessage by viewModel.feedbackMessage.observeAsState(initial = "Initial")

    Column {
        MovieTopBar(
            title = "Romantic Movies",
            onQueryChange = { query ->
                if (query.isEmpty() && feedbackMessage == "Initial") {
                    viewModel.loadAllMovies()
                }
                else {
                    viewModel.setSearchQuery(query)
                }
            },
        )
        if (moviesToShow.isNotEmpty() && feedbackMessage == "Initial") {
            MoviesGridView(moviesToShow, queryString)
        } else if (moviesToShow.isEmpty()){
            Text(text = feedbackMessage)
        } else {
            MoviesGridView(moviesToShow, queryString)
        }
    }
}