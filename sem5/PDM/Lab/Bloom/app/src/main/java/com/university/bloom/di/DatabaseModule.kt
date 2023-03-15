package com.university.bloom.di

import android.content.Context
import androidx.room.Room
import com.university.bloom.AppDatabase
import com.university.bloom.data.scooter.ScooterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideScooterDao(appDatabase: AppDatabase): ScooterDao {
        return appDatabase.scooterDao()
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "MyDatabase"
        ).build()
    }
}