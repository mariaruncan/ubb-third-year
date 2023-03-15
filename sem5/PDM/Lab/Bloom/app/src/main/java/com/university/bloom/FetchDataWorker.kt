package com.university.bloom

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.university.bloom.data.scooter.RemoteScooterDataSource
import com.university.bloom.data.scooter.ScooterDao
import com.university.bloom.utils.TAG
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@HiltWorker
class FetchDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val itemDao: ScooterDao,
    private val remoteScooterDataSource: RemoteScooterDataSource
) : Worker(appContext, workerParameters) {
    override fun doWork(): Result {
        runBlocking {
            while (true) {
                val itemsFromDb = itemDao.getAll()
                itemsFromDb.forEach {
                    if (it.shouldAdd) {
                        remoteScooterDataSource.addItem(it.toScooter())
                    } else if (it.shouldUpdate) {
                        remoteScooterDataSource.updateItem(it.toScooter())
                    }
                }
                itemDao.deleteAll()
                remoteScooterDataSource.getAll()?.forEach {
                    itemDao.insert(it.toScooterDb())
                }
                delay(5000L)
                Log.d(TAG, "fetch data worker")
            }
        }
        return Result.success()
    }
}