package com.university.bloom.di

import com.university.bloom.api.ItemApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {
    @Binds
    abstract fun provideItemApi(impl: RetrofitItemApi): ItemApi
}

class RetrofitItemApi constructor(
    itemApiDelegate: ItemApi
) : ItemApi by itemApiDelegate