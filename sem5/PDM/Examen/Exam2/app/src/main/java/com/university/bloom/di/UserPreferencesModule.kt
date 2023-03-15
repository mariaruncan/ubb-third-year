package com.university.bloom.di

import android.content.Context
import com.university.bloom.data.userpreferences.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserPreferencesModule {
    @Provides
    @Singleton
    fun provideUserPreferencesRepository(@ApplicationContext appContext: Context): UserPreferencesRepository =
        UserPreferencesRepository(appContext)
}