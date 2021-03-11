package com.android.misfinanzas.sync

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("EXPERIMENTAL_API_USAGE")
class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val syncManager by inject<SyncManager>()

    override suspend fun doWork(): Result {
        return try {
            withContext(Dispatchers.IO) {
                val syncType = SyncType.valueOf(inputData.getString(SYNC_TYPE).orEmpty())
                syncManager.sync(syncType)
                Result.success()
            }
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    companion object {
        private const val SYNC_TYPE = "SYNC_TYPE"

        fun enqueue(context: Context, type: SyncType = SyncType.SYNC_ALL): Operation {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val inputData = Data.Builder()
                .putString(SYNC_TYPE, type.name)
                .build()
            val request = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()
            return WorkManager.getInstance(context)
                .enqueue(request)
        }
    }

}
