package com.university.bloom.di

import android.content.Context
import androidx.room.Room
import com.university.bloom.AppDatabase
import com.university.bloom.data.item.ItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideItemDao(appDatabase: AppDatabase): ItemDao {
        return appDatabase.itemDao()
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "MyDatabase5"
        ).build()
    }
}