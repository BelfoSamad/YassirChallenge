package com.samadtch.yassirchallenge.data.datasources.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.samadtch.yassirchallenge.models.Movie

class MoviesPagingSource(private val moviesDatasource: MoviesDatasource) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPageNumber = params.key ?: 1
            val movies = moviesDatasource.getMovies(nextPageNumber).getOrThrow()
            //Return Results
            LoadResult.Page(
                data = movies.results,
                prevKey = null,
                nextKey = movies.page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}