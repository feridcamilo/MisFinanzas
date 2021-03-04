package com.android.misfinanzas.ui.login

sealed class LoginViewState {
    object NotLogged : LoginViewState()
    object Logged : LoginViewState()
    object WrongUserOrPassword : LoginViewState()
}
