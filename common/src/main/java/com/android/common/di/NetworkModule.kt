package com.android.common.di

import com.android.data.BuildConfig
import com.android.data.repository.MoviesRepositoryImpl
import com.android.data.source.remote.api.ApiService
import com.android.domain.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun clientInterceptor(): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val newUrl =
                request
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                    .build()

            val newRequest =
                request
                    .newBuilder()
                    .url(newUrl)
                    .build()
            chain.proceed(newRequest)
        }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit
            .Builder()
            .client(
                OkHttpClient
                    .Builder()
                    .also { client ->
                        client.addNetworkInterceptor(clientInterceptor())
                    }.build(),
            ).baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(
            ApiService::class.java,
        )

    @Provides
    @Singleton
    fun provideMovieDetailRepository(apiService: ApiService): MoviesRepository =
        MoviesRepositoryImpl(apiService)
}
