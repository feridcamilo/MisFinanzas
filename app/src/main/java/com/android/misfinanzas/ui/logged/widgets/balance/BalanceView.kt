package com.android.misfinanzas.ui.logged.widgets.balance

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.android.domain.model.Balance
import com.android.domain.utils.MoneyUtils
import com.android.misfinanzas.databinding.CardViewBalanceBinding
import com.google.android.material.card.MaterialCardView

class BalanceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    lateinit var binding: CardViewBalanceBinding

    init {
        inflate()
    }

    private fun inflate() {
        binding = CardViewBalanceBinding.inflate(LayoutInflater.from(context), this)
    }

    fun showBalance(balance: Balance) = with(binding) {
        tvCashValue.text = MoneyUtils.getMoneyFormat().format(balance.TengoEfectivo)
        tvCardValue.text = MoneyUtils.getMoneyFormat().format(balance.TengoElectronico)
        tvTotalValue.text = MoneyUtils.getMoneyFormat().format(balance.TengoTotal)
    }
}
