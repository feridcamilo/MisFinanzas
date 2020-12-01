package com.android.misfinanzas.ui.widgets.login

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.android.misfinanzas.R
import com.android.misfinanzas.ui.sync.UserCredential
import com.android.misfinanzas.utils.showShortToast
import kotlinx.android.synthetic.main.card_view_login.view.*

class LoginView(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.card_view_login, this)
    }

    fun getCredential(): UserCredential? {
        val user = et_user.text.trim().toString()
        val password = et_password.text.trim().toString()
        return if (user.isEmpty() || password.isEmpty()) {
            context?.showShortToast(R.string.info_enter_user_and_password)
            null
        } else {
            UserCredential(user, password)
        }
    }
}
