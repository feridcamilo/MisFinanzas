package com.android.data.utils

import java.text.DecimalFormat
import java.text.NumberFormat

class MoneyUtils {
    companion object {
        fun getMoneyFormat(): NumberFormat {
            return DecimalFormat("$ ###,###,##0.00")
        }
    }
}
