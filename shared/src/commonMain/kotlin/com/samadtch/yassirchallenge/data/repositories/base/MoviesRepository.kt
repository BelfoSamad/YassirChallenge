package com.samadtch.yassirchallenge.data.repositories.base

import com.samadtch.yassirchallenge.data.datasources.remote.MoviesPagingSource
import com.samadtch.yassirchallenge.models.Movie

interface MoviesRepository {

    fun getMovies(): MoviesPagingSource

    suspend fun getMovieById(id: Int): Result<Movie>

}