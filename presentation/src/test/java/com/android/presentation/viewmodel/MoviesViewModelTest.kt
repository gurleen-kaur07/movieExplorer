package com.android.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.android.common.utils.Constants
import com.android.data.source.local.dao.MovieDao
import com.android.domain.model.Dates
import com.android.domain.model.MovieItem
import com.android.domain.model.UpcomingMovieResponse
import com.android.domain.usecase.UpcomingMovieUseCase
import com.android.domain.util.Resource
import com.android.presentation.HomeState
import com.android.presentation.MoviesViewModel
import com.android.presentation.actions.MoviesIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MoviesViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    private lateinit var upcomingMovieUseCase: UpcomingMovieUseCase

    @Mock
    private lateinit var dao: MovieDao

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = MoviesViewModel(upcomingMovieUseCase, dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `processIntent should return success`() =
        testScope.runTest {
            val movies =
                listOf(
                    MovieItem(
                        overview = "In a galaxy far, far away, the battle between good and evil unfolds as heroes rise to fight for justice and freedom.",
                        movieId = "123456",
                        originalLanguage = "en",
                        originalTitle = "Star Wars: A New Hope",
                        video = false,
                        title = "Star Wars",
                        posterPath = "/path/to/poster.jpg",
                        backdropPath = "/path/to/backdrop.jpg",
                        releaseDate = "1977-05-25",
                        popularity = 88.76,
                        voteAverage = 8.6,
                        adult = false,
                        voteCount = 12000,
                    ),
                )

            val response =
                UpcomingMovieResponse(
                    results = movies,
                    dates =
                        Dates(
                            maximum = "2024-12-31",
                            minimum = "2024-01-01",
                        ),
                    page = 1,
                    totalPages = 10,
                    totalResults = 11,
                )

            `when`(upcomingMovieUseCase(Constants.MOVIE_LANG.get, 1)).thenReturn(
                Resource.Success(
                    response,
                ),
            )
            viewModel.processIntent(MoviesIntent.LoadMovies(Constants.MOVIE_LANG.get))

            val states = mutableListOf<Resource<List<MovieItem>>>()
            val job =
                launch {
                    viewModel.uiStates.collect { state ->
                        if (state is HomeState.ResultAllMovies) {
                            states.add(state.data)
                        }
                    }
                }

            advanceUntilIdle()
            assertEquals(1, states.size)
            assert(states[0] is Resource.Success)
            assertEquals(movies, (states[0] as Resource.Success).data)
            job.cancel()
        }

    @Test
    fun `on loadmovies intent, processIntent should return loading state followed by error state`() =
        testScope.runTest {
            val errorMessage = "Error while fetching movie details"
            `when`(upcomingMovieUseCase(Constants.MOVIE_LANG.get, 1)).thenReturn(
                Resource.Error(
                    errorMessage,
                ),
            )

            viewModel.uiStates.test {
                viewModel.processIntent(MoviesIntent.LoadMovies(Constants.MOVIE_LANG.get, 1))

                assertEquals(HomeState.NONE, awaitItem())
                assertEquals(HomeState.Loading(true), awaitItem())
                assertEquals(
                    HomeState.Exception(callErrors = "Error while fetching movie details"),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `on loadMovies intent, processIntent should return loading state followed by success state`() =
        testScope.runTest {
            val movies =
                listOf(
                    MovieItem(
                        overview = "In a galaxy far, far away, the battle between good and evil unfolds as heroes rise to fight for justice and freedom.",
                        movieId = "123456",
                        originalLanguage = "en",
                        originalTitle = "Star Wars: A New Hope",
                        video = false,
                        title = "Star Wars",
                        posterPath = "/path/to/poster.jpg",
                        backdropPath = "/path/to/backdrop.jpg",
                        releaseDate = "1977-05-25",
                        popularity = 88.76,
                        voteAverage = 8.6,
                        adult = false,
                        voteCount = 12000,
                    ),
                )

            val response =
                UpcomingMovieResponse(
                    results = movies,
                    dates =
                        Dates(
                            maximum = "2024-12-31",
                            minimum = "2024-01-01",
                        ),
                    page = 1,
                    totalPages = 10,
                    totalResults = 11,
                )

            val successUiState = HomeState.ResultAllMovies(Resource.Success(response.results))

            `when`(upcomingMovieUseCase(Constants.MOVIE_LANG.get, 1)).thenReturn(
                Resource.Success(
                    response,
                ),
            )

            viewModel.uiStates.test {
                viewModel.processIntent(MoviesIntent.LoadMovies(Constants.MOVIE_LANG.get, 1))

                assertEquals(HomeState.NONE, awaitItem())
                assertEquals(HomeState.Loading(true), awaitItem())
                val item = awaitItem()
                assertTrue(
                    item is HomeState.ResultAllMovies,
                )

                assertEquals(
                    response.results,
                    (successUiState.data as Resource.Success).data,
                )
            }
        }
}
