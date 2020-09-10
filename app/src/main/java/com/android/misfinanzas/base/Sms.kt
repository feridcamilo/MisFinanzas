package com.android.misfinanzas.base

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
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
        const val ID = "_id"
        const val ADDRESS = "address"
        const val BODY = "body"
        const val READ = "read"
        const val DATE = "date"
        const val TYPE = "type"
        const val INBOX = "inbox"
        const val SENT = "sent"
        const val SMS_URI = "content://sms/"

        fun getAllSms(context: Context): List<Sms> {
            val lstSms: MutableList<Sms> = ArrayList()
            val message = Uri.parse(SMS_URI)
            val cr: ContentResolver = context.contentResolver
            val c = cr.query(message, null, null, null, null)
            val totalSMS = c!!.count
            if (c.moveToFirst()) {
                for (i in 0 until totalSMS) {
                    val objSms: Sms = Sms(
                        c.getString(c.getColumnIndexOrThrow(Sms.ID)),
                        c.getString(c.getColumnIndexOrThrow(Sms.ADDRESS)),
                        c.getString(c.getColumnIndexOrThrow(Sms.BODY)),
                        c.getString(c.getColumnIndex(Sms.READ)),
                        c.getString(c.getColumnIndexOrThrow(Sms.DATE)),
                        if (c.getString(c.getColumnIndexOrThrow(Sms.TYPE)).contains("1")) Sms.INBOX else Sms.SENT
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
