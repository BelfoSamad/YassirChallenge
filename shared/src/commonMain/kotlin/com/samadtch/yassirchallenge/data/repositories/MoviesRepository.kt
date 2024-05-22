package com.samadtch.yassirchallenge.data.repositories

import com.samadtch.yassirchallenge.data.datasources.remote.MoviesDatasource
import com.samadtch.yassirchallenge.data.datasources.remote.MoviesPagingSource
import com.samadtch.yassirchallenge.data.repositories.base.MoviesRepository
import com.samadtch.yassirchallenge.di.Dispatcher
import com.samadtch.yassirchallenge.models.Movie
import kotlinx.coroutines.withContext

class MoviesRepository(
    private val moviesDatasource: MoviesDatasource,
    private val dispatcher: Dispatcher
) : MoviesRepository {

    override fun getMovies() = MoviesPagingSource(moviesDatasource)

    override suspend fun getMovieById(id: Int): Result<Movie> = withContext(dispatcher.io) {
        moviesDatasource.getMovieById(id)
    }

}