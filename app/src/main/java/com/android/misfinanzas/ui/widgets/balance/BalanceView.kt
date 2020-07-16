package com.android.misfinanzas.ui.widgets.balance

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.android.data.local.model.BalanceVO
import com.android.misfinanzas.R
import kotlinx.android.synthetic.main.balance_card_view.view.*
import java.text.DecimalFormat
import java.text.NumberFormat

class BalanceView(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.balance_card_view, this)
    }

    private var moneyFormat: NumberFormat = DecimalFormat("$ ###,###")

    fun showBalance(balance: BalanceVO) {
        tv_cash_value.text = moneyFormat.format(balance.TengoEfectivo)
        tv_card_value.text = moneyFormat.format(balance.TengoElectronico)
        tv_total_value.text = moneyFormat.format(balance.TengoTotal)
    }
}
