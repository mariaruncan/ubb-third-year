package com.university.bloom.data.scooter

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.university.bloom.FetchDataWorker
import com.university.bloom.model.Scooter
import com.university.bloom.services.ConnectionUtil
import com.university.bloom.utils.TAG
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ScooterRepository @Inject constructor(
    @ApplicationContext appContext: Context,
    private val remoteScooterDataSource: RemoteScooterDataSource,
    private val scooterDao: ScooterDao,
    private val connectionUtil: ConnectionUtil,
) {
    val context = appContext

    init {
        startFetchDataWorker()
    }

    private fun startFetchDataWorker() {
        Log.d(TAG, "starting fetch data worker....")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = OneTimeWorkRequestBuilder<FetchDataWorker>()
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }

    suspend fun getItemById(itemId: String): Scooter? {
        if (connectionUtil.isNetworkAvailable()) {
            Log.d(TAG, "getById connection available")
            val result = scooterDao.getById(itemId)
            return if (result != null) {
                Log.d(TAG, "get item from db")
                result.toScooter()
            } else {
                Log.d(TAG, "get item from remote")
                return remoteScooterDataSource.getItemById(itemId)
            }
        } else {
            Log.d(TAG, "getById connection not available")
            Log.d(TAG, "get item from db")
            val result = scooterDao.getById(itemId)
            return result?.toScooter()
        }
    }

    suspend fun getAll(): List<Scooter>? {
        if (connectionUtil.isNetworkAvailable()) {
            Log.d(TAG, "getAll connection available")

            val response = remoteScooterDataSource.getAll()
            if (response != null) {
                Log.d(TAG, "refresh items in db, itemsCount:${response.size}")
                scooterDao.deleteAll()
                response.forEach {
                    scooterDao.insert(it.toScooterDb())
                }
            }
            return response
        } else {
            Log.d(TAG, "getAll connection not available")
            Log.d(TAG, "get items from db")
            val result = scooterDao.getAll()
            return result.map { it.toScooter() }
        }
    }

    suspend fun add(number: Int, batteryLevel: Int, isLocked: Boolean, latitude: Double, longitude: Double): Scooter? {
        val item = Scooter(
            _id = "",
            number = number,
            batteryLevel = batteryLevel,
            locked = isLocked,
            latitude = latitude,
            longitude = longitude
        )
        return if (connectionUtil.isNetworkAvailable()) {
            Log.d(TAG, "add connection available")
            Log.d(TAG, "add to remote")
            val response = remoteScooterDataSource.addItem(item)
            if (response != null) {
                Log.d(TAG, "add in db with id: ${response._id}")
                scooterDao.insert(response.toScooterDb())
            }
            response
        } else {
            Log.d(TAG, "add connection not available")
            Log.d(TAG, "add only in db")
            scooterDao.insert(item.toScooterDb(shouldAdd = true))
            item.copy(_id = "db")
        }
    }

    suspend fun updateItem(
        itemId: String,
        number: Int,
        batteryLevel: Int,
        isLocked: Boolean,
        latitude: Double,
        longitude: Double
    ): Scooter? {
        val item = Scooter(
            _id = itemId,
            number = number,
            batteryLevel = batteryLevel,
            locked = isLocked,
            latitude = latitude,
            longitude = longitude
        )
        return if (connectionUtil.isNetworkAvailable()) {
            Log.d(TAG, "update connection available")
            Log.d(TAG, "update to remote")
            val response = remoteScooterDataSource.updateItem(item)
            if (response != null) {
                Log.d(TAG, "update in db with id: ${response._id}")
                scooterDao.update(response.toScooterDb())
            }
            response
        } else {
            Log.d(TAG, "update connection not available")
            Log.d(TAG, "update only in db with id: $itemId")
            scooterDao.update(item.toScooterDb(shouldUpdate = true))
            item
        }
    }
}