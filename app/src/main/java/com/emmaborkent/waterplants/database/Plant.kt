package com.emmaborkent.waterplants.database

import android.content.res.Resources
import com.emmaborkent.waterplants.R

class Plant {
    var id: Int = 0
    lateinit var name: String
    lateinit var species: String
    lateinit var image: String
    var datePlantNeedsWater: String = Resources.getSystem().getString(R.string.new_plant_set_date_hint)
    var daysToNextWater: String = "0"
    var datePlantNeedsMist: String = Resources.getSystem().getString(R.string.new_plant_set_date_hint)
    var daysToNextMist: String = "0"
}