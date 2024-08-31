package com.android.data.mapper

import com.android.data.source.local.entity.MovieEntity
import com.android.domain.model.MovieItem

object MovieMapper {
    fun MovieItem.toEntityModel(): MovieEntity =
        MovieEntity(
            overview = this.overview,
            movieId = this.movieId,
            originalLanguage = this.originalLanguage,
            originalTitle = this.originalTitle,
            isVideo = this.video,
            title = this.title,
            posterUrl = this.posterPath,
            backdropUrl = this.backdropPath.orEmpty(),
            releaseDate = this.releaseDate,
            popularity = this.popularity,
            voteAverage = this.voteAverage,
            isAdult = this.adult,
            voteCount = this.voteCount,
        )
}
