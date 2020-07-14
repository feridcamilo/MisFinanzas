package com.android.misfinanzas.ui.widgets.login

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.android.misfinanzas.R

class LoginView(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.login_card_view, this)
    }
}
