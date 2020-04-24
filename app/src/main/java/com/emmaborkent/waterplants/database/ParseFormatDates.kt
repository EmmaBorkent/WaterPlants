package com.emmaborkent.waterplants.database

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ParseFormatDates {

    /**
     * The formatted functions are used to display the date as a local date
     * The non-formatted functions use the default format of yyyy-MM-dd which is used
     * to store string dates in the database, doing so means the SQLite Date() function
     * can be used on the saved dates in the database
     */

    private val dayMonthYearFormat: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

    fun stringToDateLocalized(date: String): LocalDate {
        return LocalDate.parse(date, dayMonthYearFormat)
    }

    fun yearMonthDayToDate(year: Int, month: Int, day: Int): LocalDate {
        return LocalDate.of(year, month + 1, day)
    }

    fun dateToStringLocalized(date: LocalDate): String {
        return date.format(dayMonthYearFormat)
    }

    private val defaultFormat: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun stringToDateDefault(date: String): LocalDate {
        return LocalDate.parse(date)
    }

    fun dateToStringDefault(date: LocalDate): String {
        return date.toString()
    }

    fun dayMonthYearStringToYearMonthDayString(date: String): String {
        return LocalDate.parse(date, dayMonthYearFormat).format(defaultFormat)
    }

    fun yearMonthDayStringToDayMonthYearString(date: String): String {
        return LocalDate.parse(date, defaultFormat).format(dayMonthYearFormat)
    }
}