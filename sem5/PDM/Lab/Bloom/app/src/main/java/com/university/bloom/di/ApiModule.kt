package com.university.bloom.di

import com.university.bloom.api.AuthApi
import com.university.bloom.api.ScooterApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {
    @Binds
    abstract fun provideAuthApi(impl: RetrofitAuthApi): AuthApi

    @Binds
    abstract fun provideItemApi(impl: RetrofitScooterApi): ScooterApi
}

class RetrofitAuthApi constructor(
    authApiDelegate: AuthApi
) : AuthApi by authApiDelegate

class RetrofitScooterApi constructor(
    scooterApiDelegate: ScooterApi
) : ScooterApi by scooterApiDelegate