package com.example.movieassignment.ui.composable

import MovieViewModel
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.movieassignment.data.models.Movie
import com.example.movieassignment.utils.TextUtils

@Composable
fun MoviesGridView(movies: List<Movie>, query: String, viewModel: MovieViewModel) {
    val orientation = LocalConfiguration.current.orientation
    val columns = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 7 else 3
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val paddingInDp = 30 / density
    val shouldLoadMore = remember { mutableStateOf(false) }
    val inSearchMode by viewModel.isInSearchMode.observeAsState(false)

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.padding(start = paddingInDp.dp, end = paddingInDp.dp)
    ) {
        items(movies?.size ?: 0) { index ->
            MovieItem(movie = movies[index], context, density, query)
            if (!inSearchMode && viewModel.hasMoreDate() && index >= movies.size - 5) {
                shouldLoadMore.value = true
            }
        }
    }

    // Checks if the current items is about to end, to load the next item or json file.
    if (shouldLoadMore.value) {
        LaunchedEffect(shouldLoadMore) {
            viewModel.loadAllMovies()
            shouldLoadMore.value = false
        }
    }
}
/*
* This method is used to shown the fetched data over the screen.
*
* @param: movie   : Fetched movie items to be shown over screen.
* @param: context : The current context.
* @param: density : density to convert pixel into dp.
* @param: query   : any query if searched to highlight text else null
 */
@Composable
fun MovieItem(movie: Movie, context: Context, density: Float, query: String) {
    val imageResourceId = painterResource(id = getDrawableIdByName(movie.`poster-image`, context))
    val highlightText = TextUtils.buildHighlightedText(
        fullText = movie.name,
        query = query,
        higlightColor = Color.Yellow
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .padding(bottom = (density / 90).dp)
    ) {
        Image(
            painter = imageResourceId,
            contentDescription = movie.name,
            modifier = Modifier.size(width = 120.dp, height = 160.dp)
        )
        Text(
            text = if (query.isEmpty()) AnnotatedString(movie.name) else highlightText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = (density / 24).dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,  // Append '...' when the text exceeds the space
            color = Color.White
        )
    }
}

/*
* This method is used to provide image resource based on
* poster name else default image resource.
*
* @param: poster  : the name of poster fetched from Json file.
* @param: context : The current context.
* @return: Int    : returns the image resource id.
 */
@Composable
fun getDrawableIdByName(poster: String, context: Context): Int {
    val cleanedResourceName = poster.removeSuffix(".jpg")
    val id = context.resources.getIdentifier(
        cleanedResourceName,
        "drawable",
        context.packageName
    )

    return if (id == 0) {
        context.resources.getIdentifier(
            "placeholder_for_missing_posters",
            "drawable",
            context.packageName
        )
    } else id
}
