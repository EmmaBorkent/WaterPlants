package com.emmaborkent.waterplants.database

class Plant {

    var id: Long = 0
    lateinit var name: String
    lateinit var species: String
    lateinit var image: String
    var dayPlantNeedsWater: Long = 0
    var daysInBetweenWater: Long = 0
    var dayPlantNeedsMist: Long = 0
    var daysInBetweenMist: Long = 0

}