package com.android.misfinanzas.utils

import android.app.DatePickerDialog
import android.widget.EditText
import com.android.domain.utils.DateUtils
import com.android.misfinanzas.R
import java.util.*

fun EditText.setDateControl() {
    inputType = android.text.InputType.TYPE_NULL
    setOnClickListener {
        val strDate = text.toString()
        var calendar = Calendar.getInstance()
        if (strDate.isNotEmpty()) {
            calendar = DateUtils.getCalendarFromStringDate(strDate)
        }

        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]

        val picker = DatePickerDialog(
            context, { _, _year, _month, _day ->
                val date = context.getString(R.string.md_date, _day.toString(), (_month + 1).toString(), _year.toString())
                setText(date)
            }, year, month, day
        )
        picker.show()
    }
}
