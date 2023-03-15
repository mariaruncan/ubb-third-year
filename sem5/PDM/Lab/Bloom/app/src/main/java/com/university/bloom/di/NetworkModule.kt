package com.university.bloom.di

import android.content.Context
import com.university.bloom.services.ConnectionMonitor
import com.university.bloom.services.ConnectionUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideConnectionMonitor(@ApplicationContext appContext: Context): ConnectionMonitor {
        return ConnectionMonitor(appContext)
    }

    @Provides
    fun provideConnectionUtil(@ApplicationContext appContext: Context): ConnectionUtil {
        return ConnectionUtil(appContext)
    }
}