package com.android.domain.domain.usecase

import com.android.common.utils.Constants
import com.android.common.utils.MockMovieObject
import com.android.common.utils.Pagination
import com.android.domain.repository.MoviesRepository
import com.android.domain.usecase.UpcomingMovieUseCase
import com.android.domain.util.AppException
import com.android.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.eq

@ExperimentalCoroutinesApi
class UpcomingMoviesUseCaseTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var upcomingMovieUseCase: UpcomingMovieUseCase
    private val moviesRepository: MoviesRepository = Mockito.mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        upcomingMovieUseCase = UpcomingMovieUseCase(moviesRepository)
    }

    @Test
    fun `getMovieDetail should return success response`() =
        runTest {
            `when`(
                moviesRepository.upcomingMovies(
                    eq(Constants.MOVIE_LANG.get),
                    anyInt()
                )
            ).thenReturn(
                Resource.Success(
                    MockMovieObject.movieResponse,
                ),
            )

            val response =
                upcomingMovieUseCase.invoke(Constants.MOVIE_LANG.get, Pagination.INIT_PAGE.page)

            runCurrent()

            verify(moviesRepository).upcomingMovies(eq(Constants.MOVIE_LANG.get), anyInt())

            when (response) {
                is Resource.Error -> {
                    assert(false)
                }

                is Resource.Success -> {
                    assertEquals(Pagination.INIT_PAGE.page, response.data?.results?.size)
                }
            }
        }

    @Test
    fun `getMovieDetail should handle error response`() =
        runTest {
            `when`(
                moviesRepository.upcomingMovies(
                    eq(Constants.MOVIE_LANG.get),
                    anyInt()
                )
            ).thenReturn(
                Resource.Error(MockMovieObject.ERROR_MESSAGE),
            )

            val response =
                upcomingMovieUseCase.invoke(Constants.MOVIE_LANG.get, Pagination.INIT_PAGE.page)

            runCurrent()

            verify(moviesRepository).upcomingMovies(eq(Constants.MOVIE_LANG.get), anyInt())

            when (response) {
                is Resource.Error -> {
                    assertEquals(MockMovieObject.ERROR_MESSAGE, response.message)
                    assertNotEquals(AppException.SOCKET_TIME_OUT, response.message)
                }

                is Resource.Success -> {
                    assert(false)
                }
            }
        }
}
