package com.university.bloom

import androidx.room.Database
import androidx.room.RoomDatabase
import com.university.bloom.data.scooter.ScooterDao
import com.university.bloom.model.ScooterDbEntity

@Database(entities = [ScooterDbEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scooterDao(): ScooterDao
}