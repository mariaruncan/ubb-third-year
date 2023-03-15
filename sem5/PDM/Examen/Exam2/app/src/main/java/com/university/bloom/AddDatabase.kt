package com.university.bloom

import androidx.room.Database
import androidx.room.RoomDatabase
import com.university.bloom.data.item.ItemDao
import com.university.bloom.model.Item

@Database(entities = [Item::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}