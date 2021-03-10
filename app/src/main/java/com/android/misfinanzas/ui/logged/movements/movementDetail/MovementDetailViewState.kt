package com.android.misfinanzas.ui.logged.movements.movementDetail

sealed class MovementDetailViewState {

    class DescriptionsLoaded(val descriptions: List<String>) : MovementDetailViewState()
    object SynchronizedData : MovementDetailViewState()

}
