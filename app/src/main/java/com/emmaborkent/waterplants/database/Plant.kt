package com.emmaborkent.waterplants.database

class Plant {

    var id: Int = 0
    lateinit var name: String
    lateinit var species: String
    lateinit var image: String

    var datePlantNeedsWater: Long = 0
    var daysToNextWater: Long = 0
    var datePlantNeedsMist: Long = 0
    var daysToNextMist: Long = 0

}