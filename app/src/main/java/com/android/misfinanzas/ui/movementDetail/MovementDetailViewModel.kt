package com.android.misfinanzas.ui.movementDetail

import androidx.lifecycle.ViewModel
import com.android.data.local.repository.ILocalRepository
import com.android.data.remote.repository.IWebRepository

class MovementDetailViewModel(private val webRepo: IWebRepository, private val localRepo: ILocalRepository) : ViewModel() {
}
