package com.android.misfinanzas.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.UserSesion
import com.android.domain.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val viewState: LiveData<LoginViewState> get() = _viewState
    private val _viewState = MutableLiveData<LoginViewState>()

    fun login(user: String, password: String) {
        viewModelScope.launch {
            val response = userRepository.getCloudUser(user, password)
            if (response != null) {
                userRepository.insertUser(response)
                UserSesion.setUser(response)
                _viewState.postValue(LoginViewState.Logged)
            } else {
                _viewState.postValue(LoginViewState.WrongUserOrPassword)
            }
        }
    }

    suspend fun checkSession() {
        val user = userRepository.getUser()
        if (user == null) {
            _viewState.postValue(LoginViewState.NotLogged)
        } else {
            UserSesion.setUser(user)
            _viewState.postValue(LoginViewState.Logged)
        }
    }

}
