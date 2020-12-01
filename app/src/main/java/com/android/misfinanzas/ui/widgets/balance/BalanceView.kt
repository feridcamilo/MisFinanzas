package com.android.misfinanzas.ui.widgets.balance

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.android.domain.model.Balance
import com.android.domain.utils.MoneyUtils
import com.android.misfinanzas.R
import kotlinx.android.synthetic.main.card_view_balance.view.*

class BalanceView(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.card_view_balance, this)
    }

    fun showBalance(balance: Balance) {
        tv_cash_value.text = MoneyUtils.getMoneyFormat().format(balance.TengoEfectivo)
        tv_card_value.text = MoneyUtils.getMoneyFormat().format(balance.TengoElectronico)
        tv_total_value.text = MoneyUtils.getMoneyFormat().format(balance.TengoTotal)
    }
}
