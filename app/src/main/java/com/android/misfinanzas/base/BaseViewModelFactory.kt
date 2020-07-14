package com.android.misfinanzas.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.data.local.repository.ILocalRepository
import com.android.data.remote.repository.IWebRepository

class BaseViewModelFactory(private val webRepo: IWebRepository, private val localRepo: ILocalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(IWebRepository::class.java, ILocalRepository::class.java).newInstance(webRepo, localRepo)
    }
}
