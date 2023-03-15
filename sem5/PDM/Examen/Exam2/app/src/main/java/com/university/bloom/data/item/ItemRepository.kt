package com.university.bloom.data.item

import android.util.Log
import com.university.bloom.model.Item
import com.university.bloom.utils.TAG
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val remoteItemDataSource: RemoteItemDataSource,
    private val itemDao: ItemDao,
) {
    val itemsStream by lazy { itemDao.getAll() }

    suspend fun fetchData(): Boolean {
        Log.d(TAG, "fetch items")
        val response = remoteItemDataSource.getAll()
        response?.forEach {
            itemDao.insert(it)
        }
        return response != null
    }

    suspend fun updateItem(item: Item): Item? {
        Log.d(TAG, "update item")
        val response = remoteItemDataSource.updateItem(item)
        if (response != null) {
            itemDao.update(item)
        }
        return response
    }
}