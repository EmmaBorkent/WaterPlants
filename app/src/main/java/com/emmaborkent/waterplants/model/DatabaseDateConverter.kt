package com.emmaborkent.waterplants.model

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DatabaseDateConverter {
    // Room Database Type Converters

    // Used to change to a database date YYYY-MM-DD
    private val databaseDateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE

    // From YYYY-MM-DD to LocalDate
    @TypeConverter
    fun databaseDateStringToLocalDate(stringDate: String): LocalDate {
        return LocalDate.parse(stringDate, databaseDateFormatter)
    }

    // From LocalDate to YYYY-MM-DD
    @TypeConverter
    fun localDateToDatabaseDateString(localDate: LocalDate): String {
        return localDate.format(databaseDateFormatter)
    }
}