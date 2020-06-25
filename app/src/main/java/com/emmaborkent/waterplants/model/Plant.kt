package com.emmaborkent.waterplants.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DATABASE_TABLE_PLANTS)
data class Plant(

    var name: String,
    var species: String,
    var image: String,

    var waterEveryDays: Int,
    var waterDate: String,

    var mistEveryDays: Int,
    var mistDate: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var waterInDays: Int = 0
    var mistInDays: Int = 0
}