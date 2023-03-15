package com.university.bloom.data.item

import com.university.bloom.api.ItemApi
import com.university.bloom.model.Item
import javax.inject.Inject

class RemoteItemDataSource @Inject constructor(
    private val itemApi: ItemApi
) {
    suspend fun getAll(): List<Item>? =
        try {
            itemApi.getAll()
        } catch (e: Exception) {
            null
        }

    suspend fun updateItem(item: Item): Item? =
        try {
            itemApi.updateItem(item.id, item)
        } catch (e: Exception) {
            null
        }
}