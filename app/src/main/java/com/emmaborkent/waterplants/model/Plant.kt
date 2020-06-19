package com.emmaborkent.waterplants.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DATABASE_TABLE_PLANTS)
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    var name: String,
    var species: String,
    var image: String,

    var waterEveryDays: Int,
    var waterDate: String,
    val waterToday: Boolean,

    var mistEveryDays: Int,
    var mistDate: String,
    val mistToday: Boolean
) {
    var waterInDays: Int = 0
    var mistInDays: Int = 0
}