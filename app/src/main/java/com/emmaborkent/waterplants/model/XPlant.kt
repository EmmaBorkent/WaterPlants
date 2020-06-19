package com.emmaborkent.waterplants.model

import com.emmaborkent.waterplants.viewmodel.ParseFormatDates
import java.time.Period
import kotlin.math.absoluteValue
import kotlin.properties.Delegates
import java.time.LocalDate.now

class XPlant {
//    private val classNameTag: String = Plant::class.java.simpleName

    var id: Int = 0
    lateinit var name: String
    lateinit var species: String
    lateinit var image: String

    var needsWater: Boolean = true
    // TODO: 14-5-2020 Optimize. This is supposed to be called only on change, but is called every
    //  single time because the handler is called every time we assign to the property
    var datePlantNeedsWater: String by Delegates.observable(
        ParseFormatDates()
            .getLocalDateAsString()) { _, _, newValue ->
//        Log.d(classNameTag, "The datePlantNeedsWater old value is $oldValue and new value is $newValue")
        setNeedsWater(newValue)
    }
    var waterEveryDays = 0
    var daysToNextWater = Period.between(
        ParseFormatDates().stringToDateDefault(datePlantNeedsWater), now()).days.absoluteValue

    var needsMist: Boolean = true
    var datePlantNeedsMist: String by Delegates.observable(
        ParseFormatDates()
            .getLocalDateAsString()) { _, _, newValue ->
//        Log.d(classNameTag, "The datePlantNeedsMist old value is $oldValue and new value is $newValue")
        setNeedsMist(newValue)
    }
    var mistEveryDays = 0
    var daysToNextMist = Period.between(
        ParseFormatDates().stringToDateDefault(datePlantNeedsMist), now()).days.absoluteValue

    private var daysBetweenDateAndToday: Int = 0

    fun giveWater(plant: XPlant) {
//        Log.d(classNameTag, "giveWater datePlantNeedsWater was ${plant.datePlantNeedsWater}")
        val todayDate = now()
        plant.daysBetweenDateAndToday = Period.between(
            ParseFormatDates().stringToDateDefault(plant.datePlantNeedsWater),
            todayDate
        ).days
//        Log.d(classNameTag, "giveWater daysBetweenDateAndToday is ${plant.daysBetweenDateAndToday}")
        val nextWaterDate = todayDate.plusDays(plant.daysToNextWater.toLong())
        plant.datePlantNeedsWater = ParseFormatDates()
            .dateToStringDefault(nextWaterDate)
//        Log.d(classNameTag, "giveWater datePlantNeedsWater is ${plant.datePlantNeedsWater}")
    }

    fun undoWaterGift(plant: XPlant) {
//        Log.d(classNameTag, "undoWaterGift datePlantNeedsWater was ${plant.datePlantNeedsWater}")
        val days = plant.daysToNextWater.toLong() + plant.daysBetweenDateAndToday
//        Log.d(classNameTag, "undoWaterGift days is $days")
        val previousWaterDate =
            ParseFormatDates().stringToDateDefault(plant.datePlantNeedsWater).minusDays(days)
        plant.datePlantNeedsWater = ParseFormatDates()
            .dateToStringDefault(previousWaterDate)
//        Log.d(classNameTag, "undoWaterGift datePlantNeedsWater is ${plant.datePlantNeedsWater}")
    }

    fun giveMist(plant: XPlant) {
        val todayDate = now()
        daysBetweenDateAndToday = Period.between(
            ParseFormatDates().stringToDateDefault(plant.datePlantNeedsMist),
            todayDate
        ).days
        val nextMistDate = todayDate.plusDays(plant.daysToNextMist.toLong())
        plant.datePlantNeedsMist = ParseFormatDates()
            .dateToStringDefault(nextMistDate)
    }

    fun undoGiveMist(plant: XPlant) {
        val days = plant.daysToNextMist.toLong() + daysBetweenDateAndToday
        val previousMistDate =
            ParseFormatDates().stringToDateDefault(plant.datePlantNeedsMist).minusDays(days)
        plant.datePlantNeedsMist = ParseFormatDates()
            .dateToStringDefault(previousMistDate)
    }

    private fun setNeedsWater(date: String) {
        needsWater = checkIfDateIsTodayOrBefore(date)
//        Log.d(classNameTag, "needsWater is $needsWater")
    }

    private fun setNeedsMist(date: String) {
        needsMist = checkIfDateIsTodayOrBefore(date)
//        Log.d(classNameTag, "needsMist is $needsMist")
    }

    private fun checkIfDateIsTodayOrBefore(date: String): Boolean {
        val dayMonthYear = ParseFormatDates()
            .yearMonthDayStringToDayMonthYearString(date)
        val localDate = ParseFormatDates().stringToDateLocalized(dayMonthYear)
        val currentDate = now()
        return localDate == currentDate || localDate.isBefore(currentDate)
    }
}