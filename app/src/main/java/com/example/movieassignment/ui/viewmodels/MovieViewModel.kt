import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieassignment.data.models.Movie
import com.example.movieassignment.data.repositories.MovieRepository

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movieToShow = MutableLiveData<List<Movie>>(emptyList())
    val movieToShow: LiveData<List<Movie>> get() = _movieToShow
    val isInSearchMode : MutableLiveData<Boolean> = MutableLiveData(false)
    private val _queryString = MutableLiveData("")
    val queryString: LiveData<String> get() = _queryString
    private val _feedbackMessage = MutableLiveData<String>()
    val feedbackMessage: LiveData<String> get() = _feedbackMessage

    init {
        fetchInitialMovies()
    }

    /* This method is required to load the next json file, when user reach the bottom */
    fun loadAllMovies() {
        val newMovies = repository.fetchMoviesFromAsset()
        val currentMovies = movieToShow.value ?: emptyList()
        _movieToShow.value = currentMovies + newMovies
    }

    /* It checks for, if more data is available */
    fun hasMoreDate() = repository.hasMoreData()

    /*
    * This method is used to search query and load data accordingly.
    *
    * @param: query  : the current query if user is typing else null
    */
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

            _movieToShow.value = results
        } else {
            isInSearchMode.value = false
            repository.resetCurrentFile()
            _movieToShow.value = repository.fetchMoviesFromAsset()
        }
    }

    fun fetchPageTitle(): String {
        return repository.getTitle()
    }

    fun fetchInitialMovies() {
        _movieToShow.value = repository.fetchMoviesFromAsset()
    }
}
