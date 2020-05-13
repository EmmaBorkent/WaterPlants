package com.emmaborkent.waterplants.database

import android.util.Log
import java.time.LocalDate
import java.time.Period
import kotlin.properties.Delegates

class Plant {
    private val classNameTag: String = Plant::class.java.simpleName
    var id: Int = 0
    lateinit var name: String
    lateinit var species: String
    lateinit var image: String
    var needsWater: Boolean = true
    var datePlantNeedsWater: String by Delegates.observable(ParseFormatDates().getLocalDateAsString()) { property, oldValue, newValue ->
        Log.d(classNameTag, "The Property $property Old value $oldValue is now New value $newValue")
        // TODO: 7-5-2020 Create function to calculate if the new date means needsWater is true or false
        setNeedsWater(newValue)
    }
    lateinit var daysToNextWater: String
    var needsMist: Boolean = true
    var datePlantNeedsMist: String by Delegates.observable(ParseFormatDates().getLocalDateAsString()) { property, oldValue, newValue ->
        Log.d(classNameTag, "The Property $property Old value $oldValue is now New value $newValue")
        setNeedsMist(newValue)
    }
    lateinit var daysToNextMist: String
    var daysBetweenDateAndToday: Int = 0

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

    fun giveWater(plant: Plant) {
        val todayDate = LocalDate.now()
        plant.daysBetweenDateAndToday = Period.between(
            ParseFormatDates().stringToDateDefault(plant.datePlantNeedsWater),
            todayDate
        ).days
        val nextWaterDate = todayDate.plusDays(plant.daysToNextWater.toLong())
        plant.datePlantNeedsWater = ParseFormatDates().dateToStringDefault(nextWaterDate)
    }

    fun undoWaterGift(plant: Plant) {
        val days = plant.daysToNextWater.toLong() + plant.daysBetweenDateAndToday
        val previousWaterDate =
            ParseFormatDates().stringToDateDefault(plant.datePlantNeedsWater).minusDays(days)
        plant.datePlantNeedsWater = ParseFormatDates().dateToStringDefault(previousWaterDate)
    }

    fun giveMist(plant: Plant) {
        val todayDate = LocalDate.now()
        daysBetweenDateAndToday = Period.between(
            ParseFormatDates().stringToDateDefault(plant.datePlantNeedsMist),
            todayDate
        ).days
        val nextMistDate = todayDate.plusDays(plant.daysToNextMist.toLong())
        plant.datePlantNeedsMist = ParseFormatDates().dateToStringDefault(nextMistDate)
    }

    fun undoGiveMist(plant: Plant) {
        val days = plant.daysToNextMist.toLong() + daysBetweenDateAndToday
        val previousMistDate =
            ParseFormatDates().stringToDateDefault(plant.datePlantNeedsMist).minusDays(days)
        plant.datePlantNeedsMist = ParseFormatDates().dateToStringDefault(previousMistDate)
    }
}