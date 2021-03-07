package com.android.misfinanzas.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.domain.utils.StringUtils
import com.android.misfinanzas.models.Sms
import java.util.*

class SmsUtils(
    private val context: Context
) {

    private val ID = "_id"
    private val ADDRESS = "address"
    private val BODY = "body"
    private val READ = "read"
    private val DATE = "date"
    private val TYPE = "type"
    private val INBOX = "inbox"
    private val SENT = "sent"
    private val SMS_URI = "content://sms/"

    fun getAllSms(): List<Sms> {
        val lstSms: MutableList<Sms> = ArrayList()
        val message = Uri.parse(SMS_URI)
        val cr: ContentResolver = context.contentResolver
        val c = cr.query(message, null, "body LIKE '%Bancolombia%'", null, null)
        val totalSMS = c!!.count
        if (c.moveToFirst()) {
            for (i in 0 until totalSMS) {
                try {
                    val objSms = Sms(
                        c.getString(c.getColumnIndexOrThrow(ID)),
                        c.getString(c.getColumnIndexOrThrow(ADDRESS)),
                        c.getString(c.getColumnIndexOrThrow(BODY)),
                        c.getString(c.getColumnIndex(READ)),
                        c.getString(c.getColumnIndexOrThrow(DATE)),
                        if (c.getString(c.getColumnIndexOrThrow(TYPE)).contains(StringUtils.ONE)) INBOX else SENT
                    )
                    lstSms.add(objSms)
                } catch (e: Exception) {
                    Log.e("SmsError", e.message!!)
                }
                c.moveToNext()
            }
        }
        c.close()
        return lstSms
    }

}
