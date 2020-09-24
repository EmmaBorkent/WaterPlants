package com.emmaborkent.waterplants.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DateConverter {

    companion object {
        private var INSTANCE: DateConverter? = null
        fun getDateConverterInstance(): DateConverter {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance =
                    DateConverter()
                INSTANCE = instance
                return instance
            }
        }
    }

    // localDateFormatter is used to change to a local date. For example, the format might be '12.13.52'
    private val localDateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

    // From LocalDate to, for example, DD-MM-YYYY string
    fun localDateToViewDateString(localDate: LocalDate): String {
        return localDate.format(localDateFormatter)
    }
}