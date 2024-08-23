package com.android.domain.domain.usecase

import com.android.domain.model.Dates
import com.android.domain.model.MovieItem
import com.android.domain.model.UpcomingMovieResponse
import com.android.domain.repository.MoviesRepository
import com.android.domain.usecase.UpcomingMovieUseCase
import com.android.domain.util.Resource
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class UpcomingMovieUseCaseTest {
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

            val response = upcomingMovieUseCase.invoke("en-US/", 1)
            advanceUntilIdle()

            when (response) {
                is Resource.Error -> {
                    assert(false)
                }

                is Resource.Success -> {
                    Assert.assertEquals(1, response.data?.results?.size)
                }
            }
        }

    @Test
    fun `getMovieDetail should handle error response`() =
        runTest {
            val errorMessage = "An error occurred"
            `when`(moviesRepository.upcomingMovies("en-US/", 1)).thenReturn(
                Resource.Error(errorMessage),
            )

            val response = upcomingMovieUseCase.invoke("en-US/", 1)
            advanceUntilIdle()

            when (response) {
                is Resource.Error -> {
                    Assert.assertEquals(errorMessage, response.message)
                }
                is Resource.Success -> {
                    Assert.fail("Expected an error but got success")
                }
            }
        }
}
