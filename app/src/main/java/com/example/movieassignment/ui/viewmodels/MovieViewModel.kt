import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieassignment.data.models.Movie
import com.example.movieassignment.data.repositories.MovieRepository

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {
    val movieToShow = MutableLiveData<List<Movie>>(emptyList())
    val queryString = MutableLiveData<String>("")
    private val _feedbackMessage = MutableLiveData<String>()
    val feedbackMessage: LiveData<String> get() = _feedbackMessage

    init {
        loadAllMovies()
    }

    fun loadAllMovies() {
        movieToShow.value = repository.fetchMoviesFromAsset()
    }

    fun setSearchQuery(query: String) {
        val allMovies = repository.fetchMoviesFromAsset()
        if (query.length >= 3) {
            queryString.value = query
            movieToShow.value = allMovies.filter {
                it.name.contains(query, ignoreCase = true)
            }
            if (movieToShow.value.isNullOrEmpty()) {
                _feedbackMessage.value = "No movies found for the query."
            } else {
                _feedbackMessage.value = "Empty"
            }
        } else {
           // TODO
        }
    }
}
