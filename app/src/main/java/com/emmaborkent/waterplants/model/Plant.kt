package com.emmaborkent.waterplants.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.emmaborkent.waterplants.util.DATABASE_TABLE_PLANTS
import java.time.LocalDate

@Entity(tableName = DATABASE_TABLE_PLANTS)
@TypeConverters(DateConverter::class)
data class Plant(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var name: String = "",

    var species: String = "",

    var image: String = "",

    var waterEveryDays: Int = 1,

    var waterDate: LocalDate = LocalDate.now(),

    var mistEveryDays: Int = 1,

    var mistDate: LocalDate = LocalDate.now()

) {

    var waterInDays: Int = 0

    var mistInDays: Int = 0
}
