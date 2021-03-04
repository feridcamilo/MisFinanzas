package com.android.misfinanzas.sync

sealed class SyncState {

    object InProgress : SyncState()
    object Success : SyncState()
    class Failed(val exception: Exception) : SyncState()

}
