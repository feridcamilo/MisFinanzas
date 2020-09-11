package com.android.data.utils

import com.android.data.utils.StringUtils.Companion.EMPTY
import java.text.DecimalFormat
import java.text.NumberFormat

class MoneyUtils {
    companion object {
        fun getMoneyFormat(): NumberFormat {
            return DecimalFormat("$ ###,###,##0.00")
        }

        fun getBigDecimalStringValue(value: String): String {
            return try {
                value.toBigDecimal().toString()
            } catch (e: Exception) {
                EMPTY
            }
        }
    }
}
