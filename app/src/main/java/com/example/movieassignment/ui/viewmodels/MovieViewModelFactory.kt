import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieassignment.data.repositories.MovieRepository

class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

