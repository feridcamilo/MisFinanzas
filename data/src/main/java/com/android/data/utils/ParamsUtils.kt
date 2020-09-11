package com.android.data.utils

import com.android.data.remote.model.APIParameter
import com.android.data.remote.model.APIParameterBody
import com.android.data.utils.StringUtils.Companion.EMPTY
import java.util.*
import kotlin.collections.ArrayList

class ParamsUtils {
    companion object {

        private const val ID_CLIENTE = "@@IdCliente"
        private const val LAST_UPDATE = "@@UltimoUpdate"

        fun getParamsBody(clientId: String, lastSync: Date?): APIParameterBody {
            val params: MutableList<APIParameter> = ArrayList()
            params.add(APIParameter(ID_CLIENTE, clientId))
            if (lastSync != null) {
                val stringDate = getStringDateTimeToWeb(lastSync)
                params.add(APIParameter(LAST_UPDATE, stringDate))
            }
            return APIParameterBody(params)
        }

        fun getLastSyncToWeb(dateLastSync: Date?): String {
            return if (dateLastSync == null) EMPTY else getStringDateTimeToWeb(dateLastSync)
        }

        private fun getStringDateTimeToWeb(date: Date): String {
            return DateUtils.getDateTimeFormatToWebService().format(DateUtils.getDateTimeToWebService(date))
        }
    }
}
