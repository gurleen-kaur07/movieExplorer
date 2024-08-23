package com.android.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.domain.model.Dates
import com.android.domain.model.MovieItem
import com.android.domain.model.UpcomingMovieResponse
import com.android.domain.repository.MoviesRepository
import com.android.domain.util.Resource
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@HiltAndroidTest
class MovieRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var apiService: com.android.data.source.remote.api.ApiService

    private val moviesRepository: MoviesRepository = Mockito.mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        apiService = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMovies should return success`() =
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

            `when`(moviesRepository.upcomingMovies("en-US/", 1)).thenReturn(
                Resource.Success(
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
                    ),
                ),
            )

            val resultFlow = moviesRepository.upcomingMovies("en-US/", 1)
            advanceUntilIdle()

            when (resultFlow) {
                is Resource.Error -> {
                    assert(false)
                }

                is Resource.Success -> {
                    assertEquals(1, resultFlow.data?.results?.size)
                }
            }
        }
}
