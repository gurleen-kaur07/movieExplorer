package com.android.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.common.utils.DataStates
import com.android.data.mapper.MovieMapper.toEntityModel
import com.android.data.source.local.dao.MovieDao
import com.android.domain.model.MovieItem
import com.android.domain.usecase.UpcomingMovieUseCase
import com.android.domain.util.Resource
import com.android.presentation.actions.MoviesIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel
    @Inject
    constructor(
        private val moviesUseCase: UpcomingMovieUseCase,
        private val dao: MovieDao,
    ) : ViewModel() {
        private val _uiStates = MutableStateFlow<HomeState>(HomeState.NONE)
        val uiStates: Flow<HomeState> = _uiStates

        private val _effect: Channel<AppEffect> = Channel()
        val effect = _effect.receiveAsFlow()

        val navigator = mutableStateOf<MovieItem?>(null)

        private var movieList: List<MovieItem>? = null

        private var currentPage = 1
        private var hasMore = true

        fun processIntent(intent: MoviesIntent) {
            when (intent) {
                is MoviesIntent.LoadMovies -> {
                    viewModelScope.launch {
                        loadMoreItems(intent.language)
                    }
                }

                is MoviesIntent.NavigateToDetails -> {
                    navigator.value = intent.responseMovieItem
                }
            }
        }

        private suspend fun loadMoreItems(language: String) {
            if (!hasMore) {
                _effect.send(AppEffect.ShowToast(DataStates.PAGE_END.message))
                return
            }
            try {
                _uiStates.value = HomeState.Loading(true)
                val result = moviesUseCase.invoke(language, currentPage)
                when (result) {
                    is Resource.Error -> {
                        _uiStates.value = HomeState.Exception(result.message ?: "Unknown error")
                    }

                    is Resource.Success -> {
                        movieList = movieList?.let { currentData ->
                            currentData + (result.data?.results ?: emptyList())
                        } ?: result.data?.results
                        if (result.data?.results?.isNotEmpty() == true) {
                            movieList?.let {
                                _uiStates.value =
                                    HomeState.ResultAllMovies(Resource.Success(it))
                            }
                            movieList?.forEach {
                                dao.insertInMovies(
                                    listOf(it.toEntityModel()),
                                )
                            }
                            currentPage++
                        } else {
                            _uiStates.value = HomeState.Loading(false)
                            hasMore = false
                        }
                    }
                }
            } catch (e: Exception) {
                _uiStates.value = HomeState.Exception(e.message ?: "Unknown error")
            }
        }
    }
