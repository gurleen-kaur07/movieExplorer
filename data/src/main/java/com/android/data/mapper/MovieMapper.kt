package com.android.data.mapper

import com.android.data.source.local.entity.MovieEntity
import com.android.domain.model.MovieItem

object MovieMapper {
    fun MovieItem.toEntityModel(): MovieEntity =
        MovieEntity(
            id = 1,
            overview = this.overview,
            movieId = this.movieId,
            originalLanguage = this.originalLanguage,
            originalTitle = this.originalTitle,
            isVideo = this.video,
            title = this.title,
            posterUrl = this.posterPath,
            backdropUrl = this.backdropPath,
            releaseDate = this.releaseDate,
            popularity = this.popularity,
            voteAverage = this.voteAverage,
            isAdult = this.adult,
            voteCount = this.voteCount,
        )

    fun toDataModel(entity: MovieEntity): MovieItem =
        MovieItem(
            overview = entity.overview,
            movieId = entity.movieId,
            originalLanguage = entity.originalLanguage,
            originalTitle = entity.originalTitle,
            video = entity.isVideo,
            title = entity.title,
            posterPath = entity.posterUrl,
            backdropPath = entity.backdropUrl,
            releaseDate = entity.releaseDate,
            popularity = entity.popularity,
            voteAverage = entity.voteAverage,
            adult = entity.isAdult,
            voteCount = entity.voteCount,
        )
}
