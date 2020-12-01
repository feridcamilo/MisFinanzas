package com.android.misfinanzas.base

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.android.domain.utils.StringUtils
import java.util.*

data class Sms(
    var id: String,
    var address: String,
    var msg: String,
    var readState: String, //"0" for have not read sms and "1" for have read sms
    var time: String,
    var folderName: String
) {
    companion object {
        private const val ID = "_id"
        private const val ADDRESS = "address"
        private const val BODY = "body"
        private const val READ = "read"
        private const val DATE = "date"
        private const val TYPE = "type"
        private const val INBOX = "inbox"
        private const val SENT = "sent"
        private const val SMS_URI = "content://sms/"

        fun getAllSms(context: Context): List<Sms> {
            val lstSms: MutableList<Sms> = ArrayList()
            val message = Uri.parse(SMS_URI)
            val cr: ContentResolver = context.contentResolver
            val c = cr.query(message, null, null, null, null)
            val totalSMS = c!!.count
            if (c.moveToFirst()) {
                for (i in 0 until totalSMS) {
                    val objSms = Sms(
                        c.getString(c.getColumnIndexOrThrow(ID)),
                        c.getString(c.getColumnIndexOrThrow(ADDRESS)),
                        c.getString(c.getColumnIndexOrThrow(BODY)),
                        c.getString(c.getColumnIndex(READ)),
                        c.getString(c.getColumnIndexOrThrow(DATE)),
                        if (c.getString(c.getColumnIndexOrThrow(TYPE)).contains(StringUtils.ONE)) INBOX else SENT
                    )
                    lstSms.add(objSms)
                    c.moveToNext()
                }
            }
            c.close()
            return lstSms
        }
    }
}
