package com.emmaborkent.waterplants.database

import android.util.Log
import java.time.LocalDate
import kotlin.properties.Delegates

class Plant {
    private val classNameTag: String = Plant::class.java.simpleName
    var id: Int = 0
    lateinit var name: String
    lateinit var species: String
    lateinit var image: String
    var needsWater: Boolean = true
    var datePlantNeedsWater: String by Delegates.observable(ParseFormatDates().getLocalDateAsString()) {
            property, oldValue, newValue ->
            Log.d(classNameTag, "The Property $property Old value $oldValue is now New value $newValue")
            // TODO: 7-5-2020 Create function to calculate if the new date means needsWater is true or false
            setNeedsWater(newValue)
    }
    lateinit var daysToNextWater: String
    var needsMist: Boolean = true
    var datePlantNeedsMist: String by Delegates.observable(ParseFormatDates().getLocalDateAsString()) {
            property, oldValue, newValue ->
            Log.d(classNameTag, "The Property $property Old value $oldValue is now New value $newValue")
            setNeedsMist(newValue)
    }
    lateinit var daysToNextMist: String

    private fun setNeedsWater(date: String) {
        needsWater = checkIfDateIsTodayOrBefore(date)
    }

    private fun setNeedsMist(date: String) {
        needsMist = checkIfDateIsTodayOrBefore(date)
    }

    private fun checkIfDateIsTodayOrBefore(date: String): Boolean {
        Log.d(classNameTag, "checkIfDateIsTodayOrBefore date = $date")
        val dayMonthYear = ParseFormatDates().yearMonthDayStringToDayMonthYearString(date)
        val localDate = ParseFormatDates().stringToDateLocalized(dayMonthYear)
        val currentDate = LocalDate.now()
        return localDate == currentDate || localDate.isBefore(currentDate)
    }
}