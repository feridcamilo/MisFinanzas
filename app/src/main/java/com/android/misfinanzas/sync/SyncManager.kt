package com.android.misfinanzas.sync

import com.android.domain.UserSesion
import com.android.domain.repository.MasterRepository
import com.android.domain.repository.MovementRepository
import com.android.domain.repository.UserRepository
import com.android.domain.utils.DateUtils
import com.android.domain.utils.StringUtils.Companion.EMPTY
import com.android.domain.utils.StringUtils.Companion.POINT
import com.android.misfinanzas.utils.events.EventBus
import com.android.misfinanzas.utils.events.EventSubject
import java.util.*

class SyncManager(
    private val userRepository: UserRepository,
    private val movementRepository: MovementRepository,
    private val masterRepository: MasterRepository
) {

    suspend fun sync(type: SyncType = SyncType.SYNC_ALL) {
        try {
            publish(SyncState.InProgress)
            syncProcess(type)
            publish(SyncState.Success)
        } catch (e: Exception) {
            e.printStackTrace()
            publish(SyncState.Failed(e))
        }
    }

    private suspend fun syncProcess(type: SyncType) {
        syncServerDateTime()
        val currentDate = DateUtils.getCurrentDateTime()
        when (type) {
            SyncType.SYNC_ALL -> {
                syncMovements(currentDate)
                syncMasters(currentDate)
            }
            SyncType.SYNC_MOVEMENTS -> syncMovements(currentDate)
            SyncType.SYNC_MASTERS -> syncMasters(currentDate)
        }
    }

    private suspend fun syncServerDateTime() {
        val strServerDateTime = userRepository.getServerDateTime()
        val serverDateTime = DateUtils.getDateTimeFormat_AM_PM().parse(strServerDateTime.replace(POINT, EMPTY))!!
        UserSesion.setServerDateTime(serverDateTime)
        val gtmDiff = UserSesion.getServerTimeZone()!!.displayName
        userRepository.setDiffTimeWithServer(gtmDiff)
    }

    private suspend fun syncMovements(currentDate: Date) {
        movementRepository.upload()
        movementRepository.download()
        movementRepository.updateLastSyncMovements(currentDate)
    }

    private suspend fun syncMasters(currentDate: Date) {
        //masterRepository.upload()
        masterRepository.download()
        masterRepository.updateLastSyncMasters(currentDate)
    }

    private fun publish(state: SyncState) {
        EventBus.publish(EventSubject.SYNC, state)
    }

}
