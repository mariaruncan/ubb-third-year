package com.university.bloom.data.scooter

import com.university.bloom.api.ScooterApi
import com.university.bloom.model.Scooter
import javax.inject.Inject

class RemoteScooterDataSource @Inject constructor(
    private val scooterApi: ScooterApi
) {
    suspend fun getAll(): List<Scooter>? =
        try {
            scooterApi.getAll()
        } catch (e: Exception) {
            null
        }

    suspend fun getItemById(itemId: String): Scooter? =
        try {
            scooterApi.getById(itemId)
        } catch (e: Exception) {
            null
        }

    suspend fun addItem(item: Scooter): Scooter? =
        try {
            scooterApi.addItem(item)
        } catch (e: Exception) {
            null
        }

    suspend fun updateItem(item: Scooter): Scooter? =
        try {
            scooterApi.updateItem(item._id, item)
        } catch (e: Exception) {
            null
        }
}