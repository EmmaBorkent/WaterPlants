package com.emmaborkent.waterplants.database

class Plant {

    var id: Int = 0
    lateinit var name: String
    lateinit var species: String
    lateinit var image: String

    var datePlantNeedsWater: String = "0"
    var daysToNextWater: String = "0"
    var datePlantNeedsMist: String = "0"
    var daysToNextMist: String = "0"

}