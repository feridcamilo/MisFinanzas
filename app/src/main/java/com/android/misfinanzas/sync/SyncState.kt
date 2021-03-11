package com.android.misfinanzas.sync

sealed class SyncState {

    class Requested(val type: SyncType) : SyncState()
    object InProgress : SyncState()
    object Success : SyncState()
    class Failed(val exception: Exception) : SyncState()

}
