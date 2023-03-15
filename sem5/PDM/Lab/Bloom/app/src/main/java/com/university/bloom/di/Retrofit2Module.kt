package com.university.bloom.di

import com.google.gson.GsonBuilder
import com.university.bloom.BuildConfig
import com.university.bloom.api.AuthApi
import com.university.bloom.api.ScooterApi
import com.university.bloom.api.interceptors.TokenInterceptor
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
    fun provideAuthRetrofit(retrofit: Retrofit): RetrofitAuthApi {
        val delegate = retrofit.create(AuthApi::class.java)
        return RetrofitAuthApi(delegate)
    }

    @Provides
    @Singleton
    fun provideItemRetrofit(retrofit: Retrofit): RetrofitScooterApi {
        val delegate = retrofit.create(ScooterApi::class.java)
        return RetrofitScooterApi((delegate))
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
    fun provideHttpClient(tokenInterceptor: TokenInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .apply {
                if (BuildConfig.DEBUG) {
                    val interceptor = HttpLoggingInterceptor()
                    interceptor.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(interceptor)
                }
            }.build()
    }
}