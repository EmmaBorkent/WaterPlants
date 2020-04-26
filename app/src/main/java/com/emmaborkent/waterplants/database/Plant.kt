package com.emmaborkent.waterplants.database

import java.time.LocalDate

class Plant {
    var id: Int = 0
    lateinit var name: String
    lateinit var species: String
    lateinit var image: String
    var datePlantNeedsWater: String = DATE_HINT
    var daysToNextWater: String = "0"
    var needsWater: Boolean = checkIfDateIsTodayOrBefore(datePlantNeedsWater)
    var datePlantNeedsMist: String = DATE_HINT
    var daysToNextMist: String = "0"
    var needsMist: Boolean = checkIfDateIsTodayOrBefore(datePlantNeedsMist)

    private fun checkIfDateIsTodayOrBefore(date: String): Boolean {
        val dateObject = ParseFormatDates().stringToDateLocalized(date)
        return dateObject.isBefore(LocalDate.now()) || dateObject == LocalDate.now()
    }
}