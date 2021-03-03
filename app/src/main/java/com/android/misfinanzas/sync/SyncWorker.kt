package com.android.misfinanzas.sync

import android.content.Context
import androidx.work.*
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
            syncManager.sync()
            Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    companion object {
        fun enqueue(context: Context): Operation {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(constraints)
                .build()
            return WorkManager.getInstance(context)
                .enqueue(request)
        }
    }

}
