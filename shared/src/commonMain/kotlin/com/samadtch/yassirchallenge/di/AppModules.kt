package com.samadtch.yassirchallenge.di

import com.samadtch.yassirchallenge.data.datasources.remote.MoviesDatasource
import com.samadtch.yassirchallenge.data.repositories.base.MoviesRepository
import com.samadtch.yassirchallenge.data.repositories.MoviesRepository as MoviesRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val appModule = module {
    // HTTP Client
    single {
        HttpClient(CIO) {
            expectSuccess = true//Throw error if failed

            //Content Negotiation
            val json = Json { ignoreUnknownKeys = true }
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Application.Json)
            }
        }
    }
}

val dataModule = module {
    //Data Sources
    single<MoviesDatasource> {
        MoviesDatasource(get())
    }

    //Repositories
    single<MoviesRepository> {
        MoviesRepositoryImpl(get(), provideDispatcher())
        //or for UI Testing: FakeDataRepository()
    }
}