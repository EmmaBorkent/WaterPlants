package com.emmaborkent.waterplants.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ParseFormatDates {

    companion object {

        private var INSTANCE: ParseFormatDates? = null

        fun getParseFormatDatesInstance(): ParseFormatDates {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance =
                    ParseFormatDates()
                INSTANCE = instance
                return instance
            }
        }
    }

    /**
     * The formatted functions are used to display the date as a local date
     * The non-formatted functions use the default format of yyyy-MM-dd which is used
     * to store string dates in the database, doing so means the SQLite Date() function
     * can be used on the saved dates in the database
     */

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

    fun stringToDateLocalized(date: String): LocalDate {
        return LocalDate.parse(date, formatter)
    }

    fun dateToStringLocalized(date: LocalDate): String {
        return date.format(formatter)
    }

    private val defaultFormat: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun stringToDateDefault(date: String): LocalDate {
        return LocalDate.parse(date)
    }

    fun dateToStringDefault(date: LocalDate): String {
        return date.toString()
    }

    fun yearMonthDayStringToDayMonthYearString(dateAsYearMonthDay: String): String {
        return LocalDate.parse(dateAsYearMonthDay, defaultFormat).format(formatter)
    }

    fun getLocalDateAsString(): String {
        val localDate = LocalDate.now()
        return ParseFormatDates().dateToStringLocalized(localDate)
    }
}