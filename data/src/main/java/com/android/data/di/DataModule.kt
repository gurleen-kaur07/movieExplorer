package com.android.data.di

import android.content.Context
import com.android.data.source.local.AppDatabase
import com.android.data.source.local.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideDao(database: AppDatabase): MovieDao =
        database.movieDao()
}
