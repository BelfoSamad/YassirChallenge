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

    // Pager
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