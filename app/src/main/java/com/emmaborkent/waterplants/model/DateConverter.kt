package com.emmaborkent.waterplants.model

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DateConverter {

    // Room Database Type Converters

    // databaseDateFormatter is used to change to a database date YYYY-MM-DD
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

    // Other Converters

    // localDateFormatter is used to change to a local date. For example, the format might be '12.13.52'
    private val localDateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

    // From, for example, DD-MM-YYYY string, to LocalDate
    fun viewDateStringToLocalDate(stringDate: String): LocalDate {
        return LocalDate.parse(stringDate, localDateFormatter)
    }

    // From LocalDate to, for example, DD-MM-YYYY string
    fun localDateToViewDateString(localDate: LocalDate): String {
        return localDate.format(localDateFormatter)
    }

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

}