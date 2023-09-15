package com.example.movieassignment.data.repositories

import android.content.Context
import com.example.movieassignment.data.models.Movie
import com.example.movieassignment.data.models.MoviePageResponse
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream

class MovieRepository(private val context: Context) {

    private var currentFile = 1
    private val maxFile: Int
    init {
        maxFile = try {
            context.assets.list("")?.count {
                it.startsWith("CONTENT") && it.endsWith(".json")
            } ?: 0
        } catch (e: IOException) {
            0
        }
    }

    fun fetchMoviesFromAsset(): List<Movie> {
        if (currentFile > maxFile) return emptyList()
        val filename: String = "CONTENTLISTINGPAGE-PAGE${currentFile}.json"
        currentFile++
        val jsonString: String = loadJSONFromAsset(filename)

        val pageResponse = Gson().fromJson(jsonString, MoviePageResponse::class.java)
        val movieList = pageResponse.page.`content-items`.content
        return movieList
    }

    fun searchMovie(query: String): List<Movie> {
        val results = mutableListOf<Movie>()

        for (i in 1..maxFile) {
            val filename = "CONTENTLISTINGPAGE-PAGE${i}.json"
            val movies = loadJSONFromAsset(filename)
            val pageResponse = Gson().fromJson(movies, MoviePageResponse::class.java)
            val movieList = pageResponse?.page?.`content-items`?.content
            results.addAll(movieList?.filter {
                it.name.contains(query, ignoreCase = true)
            } ?: emptyList())

        }
        return results
    }

    fun hasMoreData() = currentFile <= maxFile

    fun resetCurrentFile() { currentFile = 1}

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
