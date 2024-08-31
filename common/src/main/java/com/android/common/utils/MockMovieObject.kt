package com.android.common.utils

import com.android.data.mapper.mapToMovieResult
import com.android.data.mapper.mapToReleaseDates
import com.android.data.model.ResponseDates
import com.android.data.model.ResponseMovieItem
import com.android.data.model.UpcomingMovieResponse
import com.android.domain.model.MovieItem
import com.android.domain.model.UpcomingMovies

object MockMovieObject {
    const val ERROR_MESSAGE = "Error while fetching movie details"

    private val movies =
        listOf(
            ResponseMovieItem(
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

    val movieResponse =
        UpcomingMovies(
            results = movies.mapToMovieResult(),
            dates =
            ResponseDates(
                maximum = "2024-12-31",
                minimum = "2024-01-01",
            ).mapToReleaseDates(),
            page = 1,
            totalPages = 10,
            totalResults = 11,
        )

    val emptyMovieResponse =
        UpcomingMovies(
            results = emptyList<ResponseMovieItem>().mapToMovieResult(),
            dates =
            ResponseDates(
                maximum = "",
                minimum = "",
            ).mapToReleaseDates(),
            page = 1,
            totalPages = 0,
            totalResults = 0,
        )

    val upcomingMovieResponse = UpcomingMovieResponse(
        responseDates = ResponseDates(
            maximum = "",
            minimum = "",
        ),
        page = 1,
        totalResults = movies.size,
        results = movies,
        totalPages = 1
    )

    val movieItem = MovieItem(
        overview = "",
        movieId = "",
        originalLanguage = "",
        originalTitle = "",
        video = false,
        title = "",
        posterPath = "",
        backdropPath = "",
        releaseDate = "",
        popularity = 0.0,
        voteAverage = 0.0,
        adult = false,
        voteCount = 0,
    )
}
