package com.android.misfinanzas.ui.login

import androidx.lifecycle.*
import com.android.domain.UserSesion
import com.android.domain.model.User
import com.android.domain.repository.UserRepository
import com.android.domain.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val credential = MutableLiveData<UserCredential>()

    fun setCredential(credential: UserCredential) {
        this.credential.value = credential
    }

    val syncUser = credential.distinctUntilChanged().switchMap {
        liveData(Dispatchers.IO) {
            if (it.user.isNotEmpty() && it.password.isNotEmpty()) {
                emit(Result.Loading)
                try {
                    val user = userRepository.getCloudUser(it.user, it.password)
                    if (user != null) {
                        insertLocalUser(user)
                    }
                    emit(Result.Success(user))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
        }
    }

    private fun insertLocalUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

}
