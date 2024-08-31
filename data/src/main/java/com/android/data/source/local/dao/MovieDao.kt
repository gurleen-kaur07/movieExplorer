package com.android.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.android.data.source.local.entity.MovieEntity

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE movieId = :id")
    suspend fun getMovieDetail(id: Int): MovieEntity?

    @Upsert
    suspend fun insertInMovies(movies: List<MovieEntity>): List<Long>

    @Query("delete from movies")
    fun deleteTable(): Int
}
