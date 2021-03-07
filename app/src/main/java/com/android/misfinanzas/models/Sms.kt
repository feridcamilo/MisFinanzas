package com.android.misfinanzas.models

data class Sms(
    var id: String,
    var address: String?,
    var msg: String,
    var readState: String, //"0" for have not read sms and "1" for have read sms
    var time: String,
    var folderName: String?
)
