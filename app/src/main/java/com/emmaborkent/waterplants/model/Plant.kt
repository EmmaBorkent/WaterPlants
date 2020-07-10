package com.emmaborkent.waterplants.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emmaborkent.waterplants.util.DATABASE_TABLE_PLANTS

@Entity(tableName = DATABASE_TABLE_PLANTS)
data class Plant(

    var name: String,
    var species: String,
    var image: String,

    var waterEveryDays: String,
    var waterDate: String,

    var mistEveryDays: String,
    var mistDate: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var waterInDays = "1"
    var mistInDays = "1"
}