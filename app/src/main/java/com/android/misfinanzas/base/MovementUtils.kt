package com.android.misfinanzas.base

import com.android.data.utils.DateUtils
import com.android.data.utils.StringUtils.Companion.COMMA
import com.android.data.utils.StringUtils.Companion.EMPTY
import com.android.data.utils.StringUtils.Companion.MONEY
import com.android.data.utils.StringUtils.Companion.POINT
import com.android.data.utils.StringUtils.Companion.SPACE
import java.math.BigDecimal
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class MovementUtils {
    companion object {
        private const val OUT_TRANSFER = "Transferencia por"
        private const val INCOME_TRANSFER = "recepcion transferencia"
        private const val BUY = "Compra por"
        private const val BUY_TC = "T.Cred"
        private const val PAYMENT = "Pago por"
        private const val PAYMENT_TC = "Pago de Tarjeta de Credito por"
        private const val WITHDRAWAL = "Retiro por"

        private const val DESC_TRANSFER = "Transferencia"
        private const val DESC_TRANSFER_REGEX = "desde cta \\*{1}\\d{0,} a cta \\d{0,}"
        private const val DESC_INCOME_TRANSFER_REGEX = "transferencia de (.+?) por \\\$"
        private const val DESC_BUY = "Compra"
        private const val DESC_BUY_REGEX = "en (.+?) \\d"
        private const val DESC_PAYMENT = "Pago"
        private const val DESC_PAYMENT_REGEX = "\\d a (.+?) desde"
        private const val DESC_PAYMENT_TC = "Pago TC"
        private const val DESC_PAYMENT_TC_REGEX = "tarjeta \\*{1}\\d{0,}"
        private const val DESC_WITHDRAWAL = "Retiro"
        private const val DESC_WITHDRAWAL_REGEX = "en (.+?) Hora"

        private const val REGEX_DATE = "(\\d{2}/\\d{2}/\\d{4})"
        private const val REGEX_MONEY = "\\\$(([1-9]\\d{0,2}(.\\d{3})*)|(([1-9]\\d*)?\\d))(([.,])\\d\\d)?"

        fun getMovementTypeFromString(text: String): Int {
            val textToCompare = text.toLowerCase(Locale.ROOT)

            if (textToCompare.contains(BUY.toLowerCase(Locale.ROOT)) && textToCompare.contains(BUY_TC.toLowerCase(Locale.ROOT))) {
                return MovementType.CREDIT_CARD_BUY
            } else if (textToCompare.contains(OUT_TRANSFER.toLowerCase(Locale.ROOT))
                || textToCompare.contains(BUY.toLowerCase(Locale.ROOT))
                || textToCompare.contains(PAYMENT.toLowerCase(Locale.ROOT))
                || textToCompare.contains(PAYMENT_TC.toLowerCase(Locale.ROOT))
            ) {
                return MovementType.CARD_OUT
            } else if (textToCompare.contains(INCOME_TRANSFER.toLowerCase(Locale.ROOT))) {
                return MovementType.CARD_INCOME
            } else if (textToCompare.contains(WITHDRAWAL.toLowerCase(Locale.ROOT))) {
                return MovementType.WITHDRAWAL
            }

            return MovementType.NOT_SELECTED
        }

        fun getMovementDescriptionFromString(text: String): String {
            val textToCompare = text.toLowerCase(Locale.ROOT)

            when {
                textToCompare.contains(OUT_TRANSFER.toLowerCase(Locale.ROOT)) -> {
                    return DESC_TRANSFER + SPACE + getStringFromString(text, DESC_TRANSFER_REGEX)
                }
                textToCompare.contains(INCOME_TRANSFER.toLowerCase(Locale.ROOT)) -> {
                    return DESC_TRANSFER + SPACE + getStringFromString(text, DESC_INCOME_TRANSFER_REGEX, 1)
                }
                textToCompare.contains(BUY.toLowerCase(Locale.ROOT)) -> {
                    return DESC_BUY + SPACE + getStringFromString(text, DESC_BUY_REGEX, 1)
                }
                textToCompare.contains(PAYMENT.toLowerCase(Locale.ROOT)) -> {
                    return DESC_PAYMENT + SPACE + getStringFromString(text, DESC_PAYMENT_REGEX, 1)
                }
                textToCompare.contains(PAYMENT_TC.toLowerCase(Locale.ROOT)) -> {
                    return DESC_PAYMENT_TC + SPACE + getStringFromString(text, DESC_PAYMENT_TC_REGEX)
                }
                textToCompare.contains(WITHDRAWAL.toLowerCase(Locale.ROOT)) -> {
                    return DESC_WITHDRAWAL + SPACE + getStringFromString(text, DESC_WITHDRAWAL_REGEX, 1)
                }
                else -> return EMPTY
            }
        }

        private fun getStringFromString(text: String, regex: String, group: Int = 0): String {
            val m: Matcher = Pattern.compile(regex).matcher(text)
            if (m.find()) {
                return m.group(group)
            }
            return EMPTY
        }

        /*
        Bancolombia le informa Compra por $136.347,00 en UNE TELCO UNE PAGO E 22:42. 12/09/2020 T.Cred *0207. Inquietudes al 0345109095/018000931987.
        Bancolombia le informa Compra por $32.900,00 en NETFLIX01*DL 05:12. 08/09/2020 T.Cred *0207. Inquietudes al 0345109095/018000931987.

        Bancolombia le informa Retiro por $600.000,00 en SURTIMAX_BA. Hora 12:04 23/04/2020 T.Deb *6129. Inquietudes al 0345109095/018000931987.
        Bancolombia le informa Retiro por $600.000,00 en AUTO_FLORP5. Hora 08:06 07/05/2020 T.Deb *6129. Inquietudes al 0345109095/018000931987.

        Bancolombia le informa Transferencia por $720,000 desde cta *8061 a cta 10033142644. 09/09/2020 15:18. Inquietudes al 0345109095/018000931987.
        Bancolombia le informa Transferencia por $64,000 desde cta *8061 a cta 10182840186. 06/08/2020 14:48. Inquietudes al 0345109095/018000931987.

        Bancolombia le informa Pago por $519,994.00 a BANCO FALABELLA S A desde cta *8061. 06/09/2020 16:09. Inquietudes al 0345109095/018000931987.
        Bancolombia le informa Pago de Tarjeta de Credito por $239,647 desde cta *8061 a la tarjeta *0207. 29/08/2020 23:39. Inquietudes al 0345109095/018000931987.

        Bancolombia te informa recepcion transferencia de ANGELAMARIA LEON por $400,000 en la cuenta *8061. 04/09/2020 11:44. Dudas 018000931987
         */

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
