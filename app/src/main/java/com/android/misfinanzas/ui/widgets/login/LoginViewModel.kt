package com.android.misfinanzas.ui.widgets.login

import androidx.lifecycle.ViewModel
import com.android.data.local.repository.ILocalRepository
import com.android.data.remote.repository.IWebRepository

class LoginViewModel(private val webRepo: IWebRepository, private val localRepo: ILocalRepository) : ViewModel() {
}
