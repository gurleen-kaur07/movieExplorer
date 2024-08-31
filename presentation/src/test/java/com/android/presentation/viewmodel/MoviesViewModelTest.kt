package com.android.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.android.common.utils.Constants
import com.android.common.utils.MockMovieObject
import com.android.common.utils.Pagination
import com.android.data.source.local.dao.MovieDao
import com.android.domain.usecase.UpcomingMovieUseCase
import com.android.domain.util.Resource
import com.android.presentation.HomeState
import com.android.presentation.MoviesViewModel
import com.android.presentation.actions.MoviesIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq

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
    fun `on loadMovies intent,when API fails, processIntent should return loading state followed by error state`() =
        testScope.runTest {
            `when`(upcomingMovieUseCase(eq(Constants.MOVIE_LANG.get), anyInt())).thenReturn(
                Resource.Error(
                    MockMovieObject.ERROR_MESSAGE,
                ),
            )

            viewModel.uiStates.test {
                viewModel.processIntent(
                    MoviesIntent.LoadMovies(
                        Constants.MOVIE_LANG.get,
                        Pagination.INIT_PAGE.page
                    )
                )

                runCurrent()

                verify(upcomingMovieUseCase).invoke(eq(Constants.MOVIE_LANG.get), anyInt())

                assertEquals(HomeState.NONE, awaitItem())
                assertEquals(
                    HomeState.Exception(callErrors = MockMovieObject.ERROR_MESSAGE),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `on loadMovies intent,when API succeeds processIntent should return loading state followed by success state`() =
        testScope.runTest {
            val response = MockMovieObject.movieResponse

            val successUiState = HomeState.ResultAllMovies(Resource.Success(response.results))

            `when`(upcomingMovieUseCase(eq(Constants.MOVIE_LANG.get), anyInt())).thenReturn(
                Resource.Success(
                    response,
                ),
            )

            viewModel.uiStates.test {
                viewModel.processIntent(
                    MoviesIntent.LoadMovies(
                        Constants.MOVIE_LANG.get,
                        Pagination.INIT_PAGE.page
                    )
                )

                runCurrent()

                verify(upcomingMovieUseCase).invoke(eq(Constants.MOVIE_LANG.get), anyInt())

                assertEquals(HomeState.NONE, awaitItem())

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

    @Test
    fun `on loadMovies intent,when API returns empty list,processIntent should return false loading state on API `() =
        testScope.runTest {
            val response = MockMovieObject.emptyMovieResponse

            `when`(upcomingMovieUseCase(eq(Constants.MOVIE_LANG.get), anyInt())).thenReturn(
                Resource.Success(response),
            )
            assertTrue(response.results.isEmpty())


            viewModel.uiStates.test {
                viewModel.processIntent(
                    MoviesIntent.LoadMovies(
                        Constants.MOVIE_LANG.get,
                        Pagination.INIT_PAGE.page
                    )
                )

                runCurrent()

                verify(upcomingMovieUseCase).invoke(eq(Constants.MOVIE_LANG.get), anyInt())

                assertEquals(HomeState.NONE, awaitItem())

                assert(!(awaitItem() as HomeState.Loading).isLoading)
            }
        }

    @Test
    fun `on navigate to details intent,processIntent should emit navigation event with selected item as an argument`() =
        testScope.runTest {

            val expectedState = MockMovieObject.movieItem
            assertEquals(viewModel.navigator.value, null)

            viewModel.processIntent(
                MoviesIntent.NavigateToDetails(MockMovieObject.movieItem)
            )
            runCurrent()

            assertEquals(viewModel.navigator.value, expectedState)
        }

    @Test
    fun `on loadMovies Intent,when API throws exception,processIntent should return loading state followed by exception state`() =
        testScope.runTest {

            `when`(upcomingMovieUseCase(eq(Constants.MOVIE_LANG.get), anyInt())).thenThrow(
                RuntimeException(MockMovieObject.ERROR_MESSAGE)
            )

            viewModel.uiStates.test {
                viewModel.processIntent(
                    MoviesIntent.LoadMovies(
                        Constants.MOVIE_LANG.get,
                        Pagination.INIT_PAGE.page
                    )
                )

                runCurrent()

                verify(upcomingMovieUseCase).invoke(eq(Constants.MOVIE_LANG.get), anyInt())

                assertEquals(HomeState.NONE, awaitItem())
                assertEquals(HomeState.Exception(MockMovieObject.ERROR_MESSAGE), awaitItem())
            }

        }


}
