package com.android.misfinanzas.base

import com.android.data.utils.DateUtils
import com.android.data.utils.StringUtils.Companion.COMMA
import com.android.data.utils.StringUtils.Companion.EMPTY
import com.android.data.utils.StringUtils.Companion.MONEY
import com.android.data.utils.StringUtils.Companion.POINT
import java.math.BigDecimal
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class MovementUtils {
    companion object {
        private const val OUT_TRANSFER = "informa Transferencia por"
        private const val INCOME_TRANSFER = "informa recepcion transferencia de"
        private const val BUY = "informa Compra por"
        private const val PAYMENT = "informa Pago por"
        private const val PAYMENT_TC = "informa Pago de Tarjeta de Credito por"

        private const val DESC_TRANSFER = "Transferencia"
        private const val DESC_BUY = "Compra"
        private const val DESC_PAYMENT = "Pago"
        private const val DESC_PAYMENT_TC = "Pago TC"

        private const val REGEX_DATE = "(\\d{2}/\\d{2}/\\d{4})"
        private const val REGEX_MONEY = "\\\$(([1-9]\\d{0,2}(.\\d{3})*)|(([1-9]\\d*)?\\d))(([.,])\\d\\d)?"

        fun getMovementTypeFromString(text: String): Int {
            val textToCompare = text.toLowerCase(Locale.ROOT)

            if (textToCompare.contains(OUT_TRANSFER.toLowerCase(Locale.ROOT))
                || textToCompare.contains(BUY.toLowerCase(Locale.ROOT))
                || textToCompare.contains(PAYMENT.toLowerCase(Locale.ROOT))
                || textToCompare.contains(PAYMENT_TC.toLowerCase(Locale.ROOT))
            ) {
                return MovementType.CARD_OUT
            } else if (textToCompare.contains(INCOME_TRANSFER.toLowerCase(Locale.ROOT))) {
                return MovementType.CARD_INCOME
            }

            return MovementType.NOT_SELECTED
        }

        fun getMovementDescriptionFromString(text: String): String {
            val textToCompare = text.toLowerCase(Locale.ROOT)

            if (textToCompare.contains(OUT_TRANSFER.toLowerCase(Locale.ROOT)) || textToCompare.contains(INCOME_TRANSFER.toLowerCase(Locale.ROOT))) {
                return DESC_TRANSFER
            } else if (textToCompare.contains(BUY.toLowerCase(Locale.ROOT))) {
                return DESC_BUY
            } else if (textToCompare.contains(PAYMENT.toLowerCase(Locale.ROOT))) {
                return DESC_PAYMENT
            } else if (textToCompare.contains(PAYMENT_TC.toLowerCase(Locale.ROOT))) {
                return DESC_PAYMENT_TC
            }

            return EMPTY
        }

        fun getDateFromString(text: String): Date? {
            val m: Matcher = Pattern.compile(REGEX_DATE).matcher(text)
            if (m.find()) {
                return DateUtils.getDateFormat().parse(m.group(0))
            }
            return null
        }

        /**
         * Receive an input string and get the money value.
         * Support 4 formats: (###,###.##) (###.###,##) (###,###) (###.###)
         * @param text any string chain
         * @return A converted BigDecimal value or zero if not matches
         */
        fun getMoneyFromString(text: String): BigDecimal {
            val m: Matcher = Pattern.compile(REGEX_MONEY).matcher(text)
            if (m.find()) {
                val value = m.group(0).replace(MONEY, EMPTY)
                val commaIndex = value.indexOf(COMMA)
                val pointIndex = value.indexOf(POINT)

                if (commaIndex != -1 && pointIndex != -1) {//contains comma and point separator
                    return if (commaIndex < pointIndex) {
                        //format ###,###.## should be ######.##
                        BigDecimal(value.replace(COMMA, EMPTY))
                    } else {
                        //format ###.###,## should be ######,## and after ######.##
                        BigDecimal(value.replace(POINT, EMPTY).replace(COMMA, POINT))
                    }
                } else if (commaIndex != -1) {//contains only comma separator
                    //format ###,### should be ######
                    return BigDecimal(value.replace(COMMA, EMPTY))
                } else if (pointIndex != -1) {//contains only point separator
                    //format ###.### should be ######
                    return BigDecimal(value.replace(POINT, EMPTY))
                }
            }
            return BigDecimal(0)
        }
    }
}
