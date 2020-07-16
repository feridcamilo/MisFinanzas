package com.android.misfinanzas.ui.widgets.balance

import androidx.lifecycle.ViewModel
import com.android.data.local.repository.ILocalRepository
import com.android.data.remote.repository.IWebRepository

class BalanceViewModel(private val webRepo: IWebRepository, private val localRepo: ILocalRepository) : ViewModel() {
}
