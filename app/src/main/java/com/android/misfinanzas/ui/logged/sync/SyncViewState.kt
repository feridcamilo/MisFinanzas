package com.android.misfinanzas.ui.logged.sync

sealed class SyncViewState {
    class ServerTimeLoaded(val serverDateTime: String) : SyncViewState()
    object AllSynced : SyncViewState()
    object MovementsSynced : SyncViewState()
    object MastersSynced : SyncViewState()
    object DiscardedCleared : SyncViewState()
}
