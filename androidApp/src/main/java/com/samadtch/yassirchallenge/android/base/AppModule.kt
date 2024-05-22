package com.samadtch.yassirchallenge.android.base

import com.samadtch.yassirchallenge.data.repositories.base.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.koin.java.KoinJavaComponent.get

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    fun provideMovieRepository(): MoviesRepository {
        return get(MoviesRepository::class.java)
    }

}