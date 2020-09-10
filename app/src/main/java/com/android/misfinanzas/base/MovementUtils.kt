package com.android.misfinanzas.base

import com.android.data.utils.DateUtils
import java.math.BigDecimal
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class MovementUtils() {
    companion object {
        const val OUT_TRANSFER = "Bancolombia le informa Transferencia por"
        const val INCOME_TRANSFER = "Bancolombia te informa recepcion transferencia de"
        const val BUY = "Bancolombia le informa Compra por"
        const val PAYMENT = "Bancolombia le informa Pago por"
        const val PAYMENT_TC = "Bancolombia le informa Pago de Tarjeta de Credito por"

        const val DESC_TRANSFER = "Transferencia"
        const val DESC_BUY = "Compra"
        const val DESC_PAYMENT = "Pago"
        const val DESC_PAYMENT_TC = "Pago TC"

        const val REGEX_DATE = "(\\d{2}/\\d{2}/\\d{4})"
        const val REGEX_MONEY = "\\\$(([1-9]\\d{0,2}(.\\d{3})*)|(([1-9]\\d*)?\\d))((\\.|\\,)\\d\\d)?"

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

            return ""
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
                val value = m.group(0).replace("$", "")
                val commaIndex = value.indexOf(",")
                val pointIndex = value.indexOf(".")

                if (commaIndex != -1 && pointIndex != -1) {//contains comma and point separator
                    return if (commaIndex < pointIndex) {
                        //format ###,###.## should be ######.##
                        BigDecimal(value.replace(",", ""))
                    } else {
                        //format ###.###,## should be ######,## and after ######.##
                        BigDecimal(value.replace(".", "").replace(",", "."))
                    }
                } else if (commaIndex != -1) {//contains only comma separator
                    //format ###,### should be ######
                    return BigDecimal(value.replace(",", ""))
                } else if (pointIndex != -1) {//contains only point separator
                    //format ###.### should be ######
                    return BigDecimal(value.replace(".", ""))
                }
            }
            return BigDecimal(0)
        }

        /*
        Bancolombia le informa Transferencia por $720,000 desde cta *8061 a cta 10033142644. 09/09/2020 15:18. Inquietudes al 0345109095/018000931987.
        Bancolombia le informa Transferencia por $64,000 desde cta *8061 a cta 10182840186. 06/08/2020 14:48. Inquietudes al 0345109095/018000931987.

        Bancolombia te informa recepcion transferencia de ANGELAMARIA LEON por $400,000 en la cuenta *8061. 04/09/2020 11:44. Dudas 018000931987

        Bancolombia le informa Compra por $32.900,00 en NETFLIX01*DL 05:12. 08/09/2020 T.Cred *0207. Inquietudes al 0345109095/018000931987.

        Bancolombia le informa Pago por $519,994.00 a BANCO FALABELLA S A desde cta *8061. 06/09/2020 16:09. Inquietudes al 0345109095/018000931987.

        Bancolombia le informa Pago de Tarjeta de Credito por $239,647 desde cta *8061 a la tarjeta *0207. 29/08/2020 23:39. Inquietudes al 0345109095/018000931987.
        */
    }
}
