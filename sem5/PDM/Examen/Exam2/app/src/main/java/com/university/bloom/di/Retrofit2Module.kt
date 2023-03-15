package com.university.bloom.di

import com.google.gson.GsonBuilder
import com.university.bloom.BuildConfig
import com.university.bloom.api.ItemApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Retrofit2Module {
    @Provides
    @Singleton
    fun provideItemRetrofit(retrofit: Retrofit): RetrofitItemApi {
        val delegate = retrofit.create(ItemApi::class.java)
        return RetrofitItemApi((delegate))
    }

    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(httpClient)
            .build()
    }

    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    val interceptor = HttpLoggingInterceptor()
                    interceptor.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(interceptor)
                }
            }.build()
    }
}