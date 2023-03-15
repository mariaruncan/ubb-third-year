package com.university.bloom.data.scooter

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.university.bloom.model.ScooterDbEntity

@Dao
interface ScooterDao {
    @Query("SELECT * FROM items")
    suspend fun getAll(): List<ScooterDbEntity>

    @Query("SELECT * FROM items WHERE _id = :itemId")
    suspend fun getById(itemId: String): ScooterDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ScooterDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<ScooterDbEntity>)

    @Update
    suspend fun update(item: ScooterDbEntity)

    @Query("DELETE FROM items WHERE _id = :id")
    suspend fun deleteById(id: String): Int

    @Query("DELETE FROM items")
    suspend fun deleteAll()

    @Query("SELECT * FROM items WHERE shouldAdd = :shouldAdd")
    suspend fun getToBeAdded(shouldAdd: Boolean = true): List<ScooterDbEntity>

    @Query("SELECT * FROM items WHERE shouldUpdate = :shouldUpdate")
    suspend fun getToBeUpdated(shouldUpdate: Boolean = true): List<ScooterDbEntity>
}