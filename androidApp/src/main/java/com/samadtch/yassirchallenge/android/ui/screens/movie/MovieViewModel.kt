package com.samadtch.yassirchallenge.android.ui.screens.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samadtch.yassirchallenge.data.repositories.base.MoviesRepository
import com.samadtch.yassirchallenge.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

    /***********************************************************************************************
     * ************************* Declarations
     */
    private var initializeCalled = false
    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    /***********************************************************************************************
     * ************************* Methods
     */
    fun initialize(id: Int) {
        if (initializeCalled) return
        initializeCalled = true

        //Get Data
        viewModelScope.launch {
            val movie = moviesRepository.getMovieById(id)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorCode = movie.exceptionOrNull(),
                    movie = movie.getOrNull(),
                )
            }
        }
    }

    /***********************************************************************************************
     * ************************* UI States
     */
    data class MovieUiState(
        val isLoading: Boolean = true,
        val errorCode: Throwable? = null,
        val movie: Movie? = null
    )

}