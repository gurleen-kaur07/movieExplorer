package com.android.presentation

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel
    @Inject
    constructor(
        private val moviesUseCase: UpcomingMovieUseCase,
        private val dao: MovieDao,
    ) : ViewModel() {
        private val _isPaginating = MutableStateFlow(false)
        val isPaginating: StateFlow<Boolean> = _isPaginating.asStateFlow()

        private val _uiStates = MutableStateFlow<HomeState>(HomeState.NONE)
        val uiStates: Flow<HomeState> = _uiStates

        private var movieList: List<MovieItem>? = null

        private var currentPage = 1
        private var hasMore = true

        suspend fun processIntent(intent: MoviesIntent) {
            when (intent) {
                is MoviesIntent.LoadMovies -> loadMoreItems(intent.language)
            }
        }

        suspend fun loadMoreItems(language: String) {
            if (_isPaginating.value || !hasMore) {
                _uiStates.value = HomeState.SideEffect(AppEffect.ShowToast(DataStates.PAGE_END.message))
                return
            }
            viewModelScope.launch(Dispatchers.IO) {
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
                            } ?: result.data?.results!!
                            if (result.data?.results?.isNotEmpty() == true) {
                                movieList?.let {
                                    _uiStates.value = HomeState.ResultAllMovies(Resource.Success(it))
                                }
                                if (currentPage == 1) {
                                    dao.deleteTable()
                                    dao.insertInMovies(listOf(movieList?.first()?.toEntityModel()!!))
                                }
                                currentPage++
                            } else {
                                _uiStates.value = HomeState.SideEffect(AppEffect.ShowToast(DataStates.NO_DATA.message))
                                hasMore = false
                            }
                        }
                    }
                } catch (e: Exception) {
//                    _uiStates.value = HomeState.SideEffect(AppEffect.NetworkError(DataStates.ERROR.message))
                    _uiStates.value = HomeState.Exception(e.message ?: "Unknown error")
                } finally {
                    _isPaginating.value = false
                }
            }
        }
    }
