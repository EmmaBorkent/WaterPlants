package com.emmaborkent.waterplants.database

class Plant {
    var id: Int = 0
    lateinit var name: String
    lateinit var species: String
    lateinit var image: String
    var needsWater: Boolean = false
    var datePlantNeedsWater: String = DATE_HINT
    var daysToNextWater: String = "0"
    var needsMist: Boolean = false
    var datePlantNeedsMist: String = DATE_HINT
    var daysToNextMist: String = "0"
}