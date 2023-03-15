package com.university.bloom.api.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddScooterRequest(
    @Json(name = "number") val number: Int,
    @Json(name = "batteryLevel") val batteryLevel: Int,
    @Json(name = "locked") val locked: Boolean,
    @Json(name = "latitude") val latitude: Double = 0.0,
    @Json(name = "longitude") val longitude: Double = 0.0,
)