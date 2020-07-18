package com.android.misfinanzas.base

import android.content.Context
import com.android.misfinanzas.R

class MovementType(
    val id: Int,
    val name: String
) {
    companion object {
        const val NOT_SELECTED = 0
        const val CASH_INCOME = 1
        const val CASH_OUT = 2
        const val CARD_INCOME = 3
        const val CARD_OUT = 4
        const val WITHDRAWAL = 5
        const val CREDIT_CARD_BUY = 6

        fun getImage(type: Int): Int {
            return when (type) {
                CASH_INCOME -> R.mipmap.ic_cash_income
                CASH_OUT -> R.mipmap.ic_cash_out
                CARD_INCOME -> R.mipmap.ic_card_income
                CARD_OUT -> R.mipmap.ic_card_out
                WITHDRAWAL -> R.mipmap.ic_transfer
                CREDIT_CARD_BUY -> R.mipmap.ic_credit_card_buy
                else -> R.mipmap.ic_money
            }
        }

        fun getList(context: Context): List<MovementType> {
            val movementList: MutableList<MovementType> = ArrayList()
            movementList.add(MovementType(NOT_SELECTED, context.getString(R.string.mt_not_selected)))
            movementList.add(MovementType(CASH_INCOME, context.getString(R.string.mt_cash_income)))
            movementList.add(MovementType(CASH_OUT, context.getString(R.string.mt_cash_out)))
            movementList.add(MovementType(CARD_INCOME, context.getString(R.string.mt_card_income)))
            movementList.add(MovementType(CARD_OUT, context.getString(R.string.mt_card_out)))
            movementList.add(MovementType(WITHDRAWAL, context.getString(R.string.mt_withdrawal)))
            movementList.add(MovementType(CREDIT_CARD_BUY, context.getString(R.string.mt_card_buy)))
            return movementList
        }
    }

    override fun toString(): String {
        return name
    }
}
