package com.university.bloom.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ScooterDbEntity(
    @PrimaryKey val _id: String = "",
    val number: Int,
    val batteryLevel: Int,
    val locked: Boolean,
    val latitude: Double,
    val longitude: Double,
    val shouldUpdate: Boolean = false,
    val shouldAdd: Boolean = false,
) {
    fun toScooter(): Scooter = Scooter(
        _id = if (shouldAdd) "db" else _id,
        number = number,
        batteryLevel = batteryLevel,
        locked = locked,
        latitude = latitude,
        longitude = longitude
    )
}