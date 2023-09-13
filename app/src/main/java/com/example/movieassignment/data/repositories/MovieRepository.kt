package com.example.movieassignment.data.repositories

import android.content.Context
import com.example.movieassignment.data.models.Movie
import com.example.movieassignment.data.models.MoviePageResponse
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream

class MovieRepository(private val context: Context) {

    fun fetchMoviesFromAsset(): List<Movie> {
        val jsonString: String = loadJSONFromAsset("CONTENTLISTINGPAGE-PAGE3.json")
        if (jsonString != null) {
            val pageResponse = Gson().fromJson(jsonString, MoviePageResponse::class.java)
            val movieList = pageResponse.page.`content-items`.content
            return movieList
        } else {
            return emptyList()
        }
    }

    private fun loadJSONFromAsset(filename: String): String {
        return try {
            val inputStream: InputStream = context.assets.open(filename)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }.toString()
    }
}