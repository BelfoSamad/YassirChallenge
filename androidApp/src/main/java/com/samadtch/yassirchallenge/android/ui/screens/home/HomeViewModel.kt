package com.samadtch.yassirchallenge.android.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.samadtch.yassirchallenge.data.repositories.base.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val moviesRepository: MoviesRepository) :
    ViewModel() {

    /**
     * UI State of the Home Screen
     * The Movie API returns data as a pages where each page contains 20 movies.
     * When reaching the last 5 movies in the list, the next page will be loaded.
     */
    val uiState = Pager(
        PagingConfig(
            initialLoadSize = 20,
            pageSize = 20,
            prefetchDistance = 5
        )
    ) {
        moviesRepository.getMovies()
    }.flow.cachedIn(viewModelScope).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PagingData.empty()
    )

}