package com.android.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.common.utils.Constants
import com.android.common.utils.MockMovieObject
import com.android.common.utils.Pagination
import com.android.data.mapper.MovieMapper.toEntityModel
import com.android.data.source.local.dao.MovieDao
import com.android.data.source.remote.api.ApiService
import com.android.domain.repository.MoviesRepository
import com.android.domain.util.Resource
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@HiltAndroidTest
class MovieRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var apiService: ApiService
    private lateinit var dao: MovieDao

    private lateinit var moviesRepository: MoviesRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        apiService = mock()
        moviesRepository = MoviesRepositoryImpl(apiService)
        dao = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMovies should return success when API call is successful`() =
        testScope.runTest {
            `when`(apiService.getUpcoming(anyInt(), eq(Constants.MOVIE_LANG.get))).thenReturn(
                MockMovieObject.upcomingMovieResponse,
            )

            val resultFlow =
                moviesRepository.upcomingMovies(Constants.MOVIE_LANG.get, Pagination.INIT_PAGE.page)

            runCurrent()

            verify(apiService).getUpcoming(anyInt(), eq(Constants.MOVIE_LANG.get))

            when (resultFlow) {
                is Resource.Error -> {
                    assert(false)
                }

                is Resource.Success -> {
                    assertEquals(Pagination.INIT_PAGE.page, resultFlow.data?.results?.size)
                }
            }
        }

    @Test
    fun `getMovies should return error when API call throw exception`() =
        testScope.runTest {
            `when`(apiService.getUpcoming(anyInt(), eq(Constants.MOVIE_LANG.get))).thenThrow(
                RuntimeException("Network Error"),
            )

            val resultFlow =
                moviesRepository.upcomingMovies(Constants.MOVIE_LANG.get, Pagination.INIT_PAGE.page)

            runCurrent()

            verify(apiService).getUpcoming(anyInt(), eq(Constants.MOVIE_LANG.get))

            when (resultFlow) {
                is Resource.Error -> {
                    assert(true)
                    assertEquals("Network Error", resultFlow.message)
                }

                is Resource.Success -> {
                    assert(false)
                }
            }
        }

    @Test
    fun `on database insert called,insert movies method should return success`() =
        runTest {
            `when`(dao.insertInMovies(MockMovieObject.movieResponse.results.map { it.toEntityModel() })).thenReturn(
                listOf(1L),
            )

            val data =
                dao.insertInMovies(
                    listOf(
                        MockMovieObject.movieResponse.results
                            .first()
                            .toEntityModel(),
                    ),
                )

            runCurrent()

            assertEquals(Pagination.INIT_PAGE.page, data.size)
        }

    @Test
    fun `on database delete data,delete table method should return success`() =
        runTest {
            `when`(dao.deleteTable()).thenReturn(Pagination.INIT_PAGE.page)

            val data = dao.deleteTable()
            runCurrent()

            assertEquals(Pagination.INIT_PAGE.page, data)
        }

    @Test
    fun `on getMovieDetail from database called,getMovieDetail method should return correct item`() =
        runTest {
            `when`(dao.getMovieDetail(Pagination.INIT_PAGE.page)).thenReturn(
                MockMovieObject.movieResponse.results
                    .first()
                    .toEntityModel(),
            )
            val data = dao.getMovieDetail(Pagination.INIT_PAGE.page)
            runCurrent()
            assertEquals(Pagination.INIT_PAGE.page, data?.id ?: 0)
        }
}
