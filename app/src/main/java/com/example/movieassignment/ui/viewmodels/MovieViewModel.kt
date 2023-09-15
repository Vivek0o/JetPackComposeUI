import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieassignment.data.models.Movie
import com.example.movieassignment.data.repositories.MovieRepository

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {
    val movieToShow = MutableLiveData<List<Movie>>(emptyList())
    val isInSearchMode : MutableLiveData<Boolean> = MutableLiveData(false)
    private val _queryString = MutableLiveData("")
    val queryString: LiveData<String> get() = _queryString
    private val _feedbackMessage = MutableLiveData<String>()
    val feedbackMessage: LiveData<String> get() = _feedbackMessage

    init {
        fetchInitialMovies()
    }

    fun loadAllMovies() {
        movieLoadUtil()
    }

    fun hasMoreDate() = repository.hasMoreData()

    fun setSearchQuery(query: String) {
        _queryString.value = query
        if (query.length >= 3) {
            isInSearchMode.value = true
            repository.resetCurrentFile()
            val results = repository.searchMovie(query)
            if (results.isNotEmpty()) {
                _feedbackMessage.value = "Movie Found"
            } else {
                _feedbackMessage.value = "No movies found for the query."
            }
            movieToShow.value = results
        } else {
            isInSearchMode.value = false
            repository.resetCurrentFile()
            movieToShow.value = repository.fetchMoviesFromAsset()
        }
    }

    private fun fetchInitialMovies() {
        movieToShow.value = repository.fetchMoviesFromAsset()
    }

    private fun movieLoadUtil() {
        val newMovies = repository.fetchMoviesFromAsset()
        val currentMovies = movieToShow.value ?: emptyList()
        movieToShow.value = currentMovies + newMovies
    }
}
