package com.samadtch.yassirchallenge.data.datasources.remote

import com.samadtch.yassirchallenge.BuildKonfig
import com.samadtch.yassirchallenge.data.datasources.MovieResponse
import com.samadtch.yassirchallenge.models.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

class MoviesDatasource(private val client: HttpClient) {

    suspend fun getMovies(page: Int): Result<MovieResponse> = try {
        Result.success(
            client.get(
                "https://api.themoviedb.org/3/discover/movie?" +
                        "api_key=${BuildKonfig.APIKey}&" +
                        "include_adult=false&" +
                        "language=en-US&" +
                        "page=$page&" +
                        "sort_by=popularity.desc"
            ) {
                contentType(ContentType.Application.Json)
            }.body()
        )
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getMovieById(id: Int): Result<Movie> = try {
        Result.success(
            client.get(
                "https://api.themoviedb.org/3/movie/$id?" +
                        "api_key=${BuildKonfig.APIKey}&" +
                        "language=en-US"
            ) {
                contentType(ContentType.Application.Json)
            }.body<Movie>()
        )
    } catch (e: Exception) {
        Result.failure(e)
    }

}