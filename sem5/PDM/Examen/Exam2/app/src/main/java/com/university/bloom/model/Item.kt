package com.university.bloom.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "items")
data class Item(
    @Json(name = "id")  @PrimaryKey val id: Int,
    @Json(name = "number") val number: String,
    @Json(name = "status")val status: String,
    @Json(name = "takenBy") val takenBy: String,
)