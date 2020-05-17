package com.example.misfinanzas.communication

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient

interface IGoogleSignIn {

    fun getGoogleSignInClient(): GoogleSignInClient

    fun getGoogleSignInAccount(): GoogleSignInAccount?
}