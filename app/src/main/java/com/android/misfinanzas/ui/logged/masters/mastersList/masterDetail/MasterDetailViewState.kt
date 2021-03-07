package com.android.misfinanzas.ui.logged.masters.mastersList.masterDetail

sealed class MasterDetailViewState {

    object MasterSaved : MasterDetailViewState()
    object SaveFailed : MasterDetailViewState()
    object SynchronizedData : MasterDetailViewState()

}
