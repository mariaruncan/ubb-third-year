package com.university.bloom.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Scooter(
    @Json(name = "_id") val _id: String,
    @Json(name = "number") val number: Int,
    @Json(name = "batteryLevel") val batteryLevel: Int,
    @Json(name = "locked") val locked: Boolean,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
) {
    fun toScooterDb(shouldUpdate: Boolean = false, shouldAdd: Boolean = false): ScooterDbEntity =
        ScooterDbEntity(
            _id = _id,
            number = number,
            batteryLevel = batteryLevel,
            locked = locked,
            latitude = latitude,
            longitude = longitude,
            shouldUpdate = shouldUpdate,
            shouldAdd = shouldAdd
        )
}