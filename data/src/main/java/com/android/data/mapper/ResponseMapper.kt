package com.android.data.mapper

import com.android.data.model.ResponseDates
import com.android.data.model.ResponseMovieItem
import com.android.data.model.UpcomingMovieResponse
import com.android.domain.model.Dates
import com.android.domain.model.MovieItem
import com.android.domain.model.UpcomingMovies

object ResponseMapper {
    fun mapToDomainModel(movies: UpcomingMovieResponse): UpcomingMovies =
        UpcomingMovies(
            dates = movies.responseDates.mapToReleaseDates(),
            page = movies.page,
            totalPages = movies.totalPages,
            results = movies.results.mapToMovieResult(),
            totalResults = movies.totalResults,
        )
}

fun ResponseDates.mapToReleaseDates() =
    Dates(
        maximum = this.maximum,
        minimum = this.minimum,
    )

fun List<ResponseMovieItem>.mapToMovieResult(): List<MovieItem> =
    arrayListOf<MovieItem>().apply {
        this@mapToMovieResult.map { movie ->
            this.add(
                MovieItem(
                    overview = movie.overview,
                    movieId = movie.movieId,
                    originalLanguage = movie.originalLanguage,
                    originalTitle = movie.originalTitle,
                    video = movie.video,
                    title = movie.title,
                    posterPath = movie.posterPath,
                    backdropPath = movie.backdropPath.orEmpty(),
                    releaseDate = movie.releaseDate,
                    popularity = movie.popularity,
                    voteAverage = movie.voteAverage,
                    adult = movie.adult,
                    voteCount = movie.voteCount,
                ),
            )
        }
    }
